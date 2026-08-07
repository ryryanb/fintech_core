package com.ryanbondoc.fintech.customer.controller;



import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.service.AccountService;
import com.ryanbondoc.fintech.customer.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    // POST /customers
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customer) {
        CustomerResponse createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(201).body(createdCustomer);
    }

    // GET /customers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // GET /customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(customer);
    }

    // PUT /customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable String id, @RequestBody CustomerRequest customer) {
        customerService.updateCustomer(id, customer);
        return ResponseEntity.status(204).build();
    }

    // DELETE /customers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(204).build();
    }

    // GET /customers/search
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String query) {
        return ResponseEntity.ok(customerService.searchCustomers(query));
    }

    // GET /customers/{id}/addresses
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<AddressResponse>> getCustomerAddresses(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerAddresses(id));
    }

    @PostMapping("/{id}/addresses")
public ResponseEntity<AddressResponse> addCustomerAddress(
        @PathVariable UUID id,
        @RequestBody AddressRequest address) {

    AddressResponse createdAddress =
            customerService.addCustomerAddress(id, address);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(createdAddress);
}
}
