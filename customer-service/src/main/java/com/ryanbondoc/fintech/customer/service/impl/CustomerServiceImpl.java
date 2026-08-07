package com.ryanbondoc.fintech.customer.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ryanbondoc.fintech.core.exception.BusinessException;
import com.ryanbondoc.fintech.core.exception.CustomerNotFoundException;
import com.ryanbondoc.fintech.core.exception.ResourceNotFoundException;
import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Address;
import com.ryanbondoc.fintech.customer.entity.Customer;
import com.ryanbondoc.fintech.customer.enums.KycStatus;
import com.ryanbondoc.fintech.customer.mapper.AddressMapper;
import com.ryanbondoc.fintech.customer.mapper.CustomerMapper;
import com.ryanbondoc.fintech.customer.repository.AddressRepository;
import com.ryanbondoc.fintech.customer.repository.CustomerRepository;
import com.ryanbondoc.fintech.customer.service.AccountService;
import com.ryanbondoc.fintech.customer.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService; // Will be injected later
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

 
    @Override
    public CustomerResponse createCustomer(CustomerRequest customerReq) {
        
        Customer customer = customerMapper.toEntity(customerReq);
        // Rule 1: Duplicate email not allowed
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        // Rule 2: Duplicate customer number not allowed
        if (customerRepository.existsByCustomerNumber(customer.getCustomerNumber())) {
            throw new BusinessException("Customer number already exists");
        }

        Customer createdCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(createdCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerRequest customerReq) {
        Customer existingCustomer = customerRepository.findByCustomerNumber(customerId)
            .orElseThrow(() -> new BusinessException("Customer not found"));

        Customer customer = customerMapper.toEntity(customerReq);
        
        // Rule 3: Email becomes immutable after KYC verification
        if (KycStatus.VERIFIED.equals(existingCustomer.getKycStatus()) && !customer.getEmail().equals(existingCustomer.getEmail())) {
            throw new BusinessException("Email cannot be changed after KYC verification");
        }

        CustomerResponse resp = customerMapper.toResponse(customerRepository.save(customer));

        return resp;
    }

    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findByCustomerNumber(customerId)
            .orElseThrow(() -> new BusinessException("Customer not found"));

        // Rule 5: Delete customer with active accounts (TODO)
        // This is intentionally left as a TODO since Account Service doesn't exist yet
        // if (accountService.hasActiveAccounts(customerId)) {
        //     throw new BusinessException("Customer has active accounts and cannot be deleted");
        // }

        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new CustomerNotFoundException("Customer not found: " + id));

    return customerMapper.toResponse(customer);
}

@Override
public List<CustomerResponse> searchCustomers(String query) {

    return customerRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
            .stream()
            .map(customerMapper::toResponse)
            .toList();
}

@Override
public List<AddressResponse> getCustomerAddresses(UUID customerId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() ->
                    new CustomerNotFoundException(
                            "Customer not found: " + customerId));

    return addressRepository.findByCustomer(customer)
            .stream()
            .map(addressMapper::toResponse)
            .toList();
}

@Override
@Transactional
public AddressResponse addCustomerAddress(UUID customerId,
                                          AddressRequest request) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Customer not found: " + customerId));

    Address address = addressMapper.toEntity(request);

    address.setCustomer(customer);

    Address savedAddress = addressRepository.save(address);

    return addressMapper.toResponse(savedAddress);
}

}
