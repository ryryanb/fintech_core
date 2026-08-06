package com.ryanbondoc.fintech.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryanbondoc.fintech.customer.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByCustomerId(Long customerId);
}
