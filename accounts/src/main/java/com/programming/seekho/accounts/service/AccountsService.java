package com.programming.seekho.accounts.service;

import com.programming.seekho.accounts.dto.CustomerDto;

public interface AccountsService {
    void createAccount(CustomerDto customerDto);
    CustomerDto fetchAccount(String mobileNumber);
    boolean updateAccount(CustomerDto customerDto);
    boolean deleteAccount(String mobileNumber);
}
