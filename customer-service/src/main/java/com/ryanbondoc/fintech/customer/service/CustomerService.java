package com.ryanbondoc.fintech.customer.service;

import java.util.List;
import java.util.UUID;

import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Customer;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customer);
    Customer updateCustomer(String customerId, Customer customer);
    void deleteCustomer(String customerId);
    List<CustomerResponse> getAllCustomers() ;
    CustomerResponse getCustomerById(UUID id);
}
