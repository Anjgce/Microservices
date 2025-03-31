package com.programming.seekho.accounts.service.impl;

import com.programming.seekho.accounts.constants.AccountsConstants;
import com.programming.seekho.accounts.dto.AccountsDto;
import com.programming.seekho.accounts.dto.CustomerDto;
import com.programming.seekho.accounts.dto.ResponseDto;
import com.programming.seekho.accounts.entity.Accounts;
import com.programming.seekho.accounts.entity.Customer;
import com.programming.seekho.accounts.exception.CustomerAlreadyExistsException;
import com.programming.seekho.accounts.exception.ResourceNotFoundException;
import com.programming.seekho.accounts.mapper.AccountsMapper;
import com.programming.seekho.accounts.mapper.CustomerMapper;
import com.programming.seekho.accounts.repository.AccountsRepository;
import com.programming.seekho.accounts.repository.CustomerRepository;
import com.programming.seekho.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number : "+customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setCustomerId(customer.getCustomerId());
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVING);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        //First I will check whether customer is already registered with same mobile number
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        // If customer is registered with same mobile number then I want to find what is the account details
        Accounts accounts = accountsRepository
                .findByCustomerId(customer.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        // Now directly we cannot send the entity class data to the client
        // because it may contain some sensitive information
        // so I need to convert into dto pattern

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    //This method is for updating account except account number
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null){
            Accounts accounts = accountsRepository
                    .findById(accountsDto.getAccountNumber())
                    .orElseThrow(
                            ()->new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
                    );

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            //saving data to the database and return again into the accounts object
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    ()->new ResourceNotFoundException("Customer", "CustomerId", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    //This method is for delete account
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
