package com.programming.seekho.accounts.service.impl;

import com.programming.seekho.accounts.dto.AccountsDto;
import com.programming.seekho.accounts.dto.CardsDto;
import com.programming.seekho.accounts.dto.CustomerDetailsDto;
import com.programming.seekho.accounts.dto.LoansDto;
import com.programming.seekho.accounts.entity.Accounts;
import com.programming.seekho.accounts.entity.Customer;
import com.programming.seekho.accounts.exception.ResourceNotFoundException;
import com.programming.seekho.accounts.mapper.AccountsMapper;
import com.programming.seekho.accounts.mapper.CustomerMapper;
import com.programming.seekho.accounts.repository.AccountsRepository;
import com.programming.seekho.accounts.repository.CustomerRepository;
import com.programming.seekho.accounts.service.ICustomersService;
import com.programming.seekho.accounts.service.client.CardsFeignClient;
import com.programming.seekho.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
