package com.ryanbondoc.fintech.customer.service.impl;

import org.springframework.stereotype.Service;

import com.ryanbondoc.fintech.customer.service.AccountService;



@Service
public class AccountServiceStub implements AccountService {
    @Override
    public boolean hasActiveAccounts(String customerId) {
        // Return false to prevent CustomerService from throwing errors during deletion
        return false;
    }
}
