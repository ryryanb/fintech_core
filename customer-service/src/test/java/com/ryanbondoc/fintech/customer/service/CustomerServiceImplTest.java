package com.ryanbondoc.fintech.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ryanbondoc.fintech.core.exception.BusinessException;
import com.ryanbondoc.fintech.core.exception.CustomerNotFoundException;
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

@Test
void shouldRegisterCustomerWithUniqueCustomerNumber() {
    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE",
        "CUST-10001"
    );

    Customer customer = new Customer();
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10001");

    when(customerRepository.existsByCustomerNumber("CUST-10001"))
        .thenReturn(false);

    when(customerMapper.toEntity(request))
        .thenReturn(customer);

    when(customerRepository.save(customer))
        .thenReturn(customer);

    customerService.createCustomer(request);

    verify(customerRepository).existsByCustomerNumber("CUST-10001");
    verify(customerRepository).save(customer);
}

  @Test
void shouldRejectDuplicateCustomerNumber() {
    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE",
        "CUST-10001"
    );

    Customer customer = new Customer();
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10001");

    when(customerMapper.toEntity(request))
        .thenReturn(customer);

    // Email is unique, so the service should proceed to the
    // customer-number uniqueness check.
    when(customerRepository.existsByEmail("ryan@example.com"))
        .thenReturn(false);

    // Customer number already exists.
    when(customerRepository.existsByCustomerNumber("CUST-10001"))
        .thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> customerService.createCustomer(request)
    );

    verify(customerRepository)
        .existsByEmail("ryan@example.com");

    verify(customerRepository)
        .existsByCustomerNumber("CUST-10001");

    verify(customerRepository, never())
        .save(any(Customer.class));
}

@Test
void shouldRejectDuplicateEmail() {
    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE",
        "CUST-10002"
    );

    Customer customer = new Customer();
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10002");

    when(customerMapper.toEntity(request))
        .thenReturn(customer);

    // Email already exists.
    when(customerRepository.existsByEmail("ryan@example.com"))
        .thenReturn(true);

    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> customerService.createCustomer(request)
    );

    assertEquals(
        "Email already exists",
        exception.getMessage()
    );

    verify(customerRepository)
        .existsByEmail("ryan@example.com");

    // Customer number check should not be reached.
    verify(customerRepository, never())
        .existsByCustomerNumber("CUST-10002");

    verify(customerRepository, never())
        .save(any(Customer.class));
}

@Test
void shouldRegisterCustomerWithUniqueEmail() {
    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE",
        "CUST-10002"
    );

    UUID customerId = UUID.randomUUID();

    Customer customer = new Customer();
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10002");

    CustomerResponse response = new CustomerResponse(
        customerId,
        "Ryan",
        "Bondoc",
        "ACTIVE",
        "ryan@example.com",
        "CUST-10002"
    );

    when(customerMapper.toEntity(request))
        .thenReturn(customer);

    when(customerRepository.existsByEmail("ryan@example.com"))
        .thenReturn(false);

    when(customerRepository.existsByCustomerNumber("CUST-10002"))
        .thenReturn(false);

    when(customerRepository.save(customer))
        .thenReturn(customer);

    when(customerMapper.toResponse(customer))
        .thenReturn(response);

    CustomerResponse result =
        customerService.createCustomer(request);

    assertNotNull(result);
    assertEquals(customerId, result.id());
    assertEquals("Ryan", result.firstName());
    assertEquals("Bondoc", result.lastName());
    assertEquals("ryan@example.com", result.email());
    assertEquals("ACTIVE", result.status());
    assertEquals("CUST-10002", result.customerNumber());

    verify(customerRepository)
        .existsByEmail("ryan@example.com");

    verify(customerRepository)
        .existsByCustomerNumber("CUST-10002");

    verify(customerRepository)
        .save(customer);

    verify(customerMapper)
        .toResponse(customer);
}

@Test
void shouldRetrieveCustomer() {

    // Given
    UUID customerId = UUID.randomUUID();

    Customer customer = new Customer();
    customer.setId(customerId);
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10001");

    CustomerResponse response = new CustomerResponse(
        customerId,
        "Ryan",
        "Bondoc",
        "ACTIVE",
        "ryan@example.com",
        "CUST-10001"
    );

    when(customerRepository.findById(customerId))
        .thenReturn(java.util.Optional.of(customer));

    when(customerMapper.toResponse(customer))
        .thenReturn(response);

    // When
    CustomerResponse result =
        customerService.getCustomerById(customerId);

    // Then
    assertNotNull(result);
    assertEquals(customerId, result.id());
    assertEquals("Ryan", result.firstName());
    assertEquals("Bondoc", result.lastName());
    assertEquals("ryan@example.com", result.email());
    assertEquals("ACTIVE", result.status());
    assertEquals("CUST-10001", result.customerNumber());

    verify(customerRepository)
        .findById(customerId);

    verify(customerMapper)
        .toResponse(customer);
}

@Test
void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {

    // Given
    UUID customerId = UUID.randomUUID();

    when(customerRepository.findById(customerId))
        .thenReturn(java.util.Optional.empty());

    // When / Then
    CustomerNotFoundException exception = assertThrows(
        CustomerNotFoundException.class,
        () -> customerService.getCustomerById(customerId)
    );

    assertEquals(
    "Customer not found: " + customerId,
    exception.getMessage()
);

    verify(customerRepository)
        .findById(customerId);

    verify(customerMapper, never())
        .toResponse(any(Customer.class));
}

@Test
void shouldUpdateCustomer() {

    // Given
    UUID customerId = UUID.randomUUID();

    CustomerRequest request = new CustomerRequest(
        "Ryan Updated",
        "Bondoc Updated",
        "ryan.updated@example.com",
        "ACTIVE",
        "CUST-10001"
    );

    Customer customer = new Customer();
    customer.setId(customerId);
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10001");

    CustomerResponse response = new CustomerResponse(
        customerId,
        "Ryan Updated",
        "Bondoc Updated",
        "ACTIVE",
        "ryan.updated@example.com",
        "CUST-10001"
    );

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.of(customer));

    when(customerRepository.existsByEmailAndIdNot(
        "ryan.updated@example.com",
        customerId))
        .thenReturn(false);

    when(customerRepository.save(customer))
        .thenReturn(customer);

    when(customerMapper.toResponse(customer))
        .thenReturn(response);

    // When
    CustomerResponse result =
        customerService.updateCustomer(customerId, request);

    // Then
    assertNotNull(result);
    assertEquals(customerId, result.id());
    assertEquals("Ryan Updated", result.firstName());
    assertEquals("Bondoc Updated", result.lastName());
    assertEquals("ryan.updated@example.com", result.email());
    assertEquals("ACTIVE", result.status());
    assertEquals("CUST-10001", result.customerNumber());

    assertEquals("Ryan Updated", customer.getFirstName());
    assertEquals("Bondoc Updated", customer.getLastName());
    assertEquals("ryan.updated@example.com", customer.getEmail());

    verify(customerRepository)
        .findById(customerId);

    verify(customerRepository)
        .existsByEmailAndIdNot(
            "ryan.updated@example.com",
            customerId);

    verify(customerRepository)
        .save(customer);

    verify(customerMapper)
        .toResponse(customer);
}

@Test
void shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistentCustomer() {

    // Given
    UUID customerId = UUID.randomUUID();

    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE",
        "CUST-10001"
    );

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.empty());

    // When / Then
    CustomerNotFoundException exception = assertThrows(
        CustomerNotFoundException.class,
        () -> customerService.updateCustomer(customerId, request)
    );

    assertEquals(
        "Customer not found: " + customerId,
        exception.getMessage()
    );

    verify(customerRepository)
        .findById(customerId);

    verify(customerRepository, never())
        .existsByEmailAndIdNot(any(String.class), any(UUID.class));

    verify(customerRepository, never())
        .save(any(Customer.class));

    verify(customerMapper, never())
        .toResponse(any(Customer.class));
}

@Test
void shouldRejectUpdateWhenEmailAlreadyExists() {

    // Given
    UUID customerId = UUID.randomUUID();

    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "existing@example.com",
        "ACTIVE",
        "CUST-10001"
    );

    Customer customer = new Customer();
    customer.setId(customerId);
    customer.setFirstName("Ryan");
    customer.setLastName("Bondoc");
    customer.setEmail("ryan@example.com");
    customer.setStatus("ACTIVE");
    customer.setCustomerNumber("CUST-10001");

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.of(customer));

    when(customerRepository.existsByEmailAndIdNot(
        "existing@example.com",
        customerId))
        .thenReturn(true);

    // When / Then
    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> customerService.updateCustomer(customerId, request)
    );

    assertEquals(
        "Email already exists",
        exception.getMessage()
    );

    verify(customerRepository)
        .findById(customerId);

    verify(customerRepository)
        .existsByEmailAndIdNot(
            "existing@example.com",
            customerId);

    verify(customerRepository, never())
        .save(any(Customer.class));

    verify(customerMapper, never())
        .toResponse(any(Customer.class));
}
}