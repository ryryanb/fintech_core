package com.ryanbondoc.fintech.customer.service;

import java.util.List;
import java.util.UUID;

import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customer);
    CustomerResponse updateCustomer(UUID customerId, CustomerRequest customer);
    void deleteCustomer(String customerId);
    List<CustomerResponse> getAllCustomers() ;
    CustomerResponse getCustomerById(UUID id);
    List<CustomerResponse> searchCustomers(String query);
    List<AddressResponse> getCustomerAddresses(UUID customerId);
    AddressResponse addCustomerAddress(UUID customerId,
                                          AddressRequest request);
}
