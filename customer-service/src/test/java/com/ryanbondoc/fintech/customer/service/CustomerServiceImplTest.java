package com.ryanbondoc.fintech.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Customer;
import com.ryanbondoc.fintech.customer.mapper.CustomerMapper;
import com.ryanbondoc.fintech.customer.repository.CustomerRepository;
import com.ryanbondoc.fintech.customer.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldRegisterCustomer() {

        // Given
        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                "ryan@example.com",
                "ACTIVE",
                null
        );

        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setFirstName("Ryan");
        customer.setLastName("Bondoc");
        customer.setEmail("ryan@example.com");
        customer.setStatus("ACTIVE");
        customer.setCustomerNumber(null);

        Customer savedCustomer = new Customer();
        savedCustomer.setId(customerId);
        savedCustomer.setFirstName("Ryan");
        savedCustomer.setLastName("Bondoc");
        savedCustomer.setEmail("ryan@example.com");
        savedCustomer.setStatus("ACTIVE");
        savedCustomer.setCustomerNumber(null);

        CustomerResponse response = new CustomerResponse(
                customerId,
                "Ryan",
                "Bondoc",
                "ACTIVE",
                "ryan@example.com",
                null
        );

        when(customerMapper.toEntity(request))
                .thenReturn(customer);

        when(customerRepository.save(customer))
                .thenReturn(savedCustomer);

        when(customerMapper.toResponse(savedCustomer))
                .thenReturn(response);

        // When
        CustomerResponse result =
                customerService.createCustomer(request);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("Ryan", result.firstName());
        assertEquals("Bondoc", result.lastName());
        assertEquals("ryan@example.com", result.email());
        assertEquals("ACTIVE", result.status());
        assertEquals(null, result.customerNumber());

        verify(customerMapper).toEntity(request);
        verify(customerRepository).save(customer);
        verify(customerMapper).toResponse(savedCustomer);
    }
}