package com.ryanbondoc.fintech.customer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryanbondoc.fintech.customer.entity.Address;
import com.ryanbondoc.fintech.customer.entity.Customer;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    //Address findByCustomerId(Long customerId);
    List<Address> findByCustomerId(UUID customerId);
    List<Address> findByCustomer(Customer customer);
}
