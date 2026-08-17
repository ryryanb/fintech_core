package com.ryanbondoc.fintech.customer.mapper;

import org.springframework.stereotype.Component;

import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Customer;

@Component
public class CustomerMapper {
    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getStatus(), customer.getEmail(), customer.getCustomerNumber()
        );
    }

    public Customer toEntity(CustomerRequest request) {
    Customer customer = new Customer();
    customer.setFirstName(request.firstName());
    customer.setLastName(request.lastName());
    customer.setEmail(request.email());
    customer.setStatus(request.status());
    customer.setCustomerNumber(request.customerNumber());
    return customer;
}
}
