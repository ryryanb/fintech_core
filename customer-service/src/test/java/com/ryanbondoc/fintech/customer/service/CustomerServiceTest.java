/*package com.ryanbondoc.fintech.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ryanbondoc.fintech.core.exception.CustomerNotFoundException;
import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Customer;
import com.ryanbondoc.fintech.customer.enums.KycStatus;
import com.ryanbondoc.fintech.customer.mapper.AddressMapper;
import com.ryanbondoc.fintech.customer.mapper.CustomerMapper;
import com.ryanbondoc.fintech.customer.repository.AddressRepository;
import com.ryanbondoc.fintech.customer.repository.CustomerRepository;
import com.ryanbondoc.fintech.customer.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private CustomerRequest testCustomerRequest;

    private UUID customerId;

    @BeforeEach
void setUp() {
    testCustomerRequest = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE", "12345678"
    );
    customerId = UUID.randomUUID();

    testCustomer = new Customer();

    testCustomer.setId(UUID.randomUUID());
    testCustomer.setCustomerNumber("CUST-1000001");
    testCustomer.setFirstName("Ryan");
    testCustomer.setLastName("Bondoc");
    testCustomer.setEmail("ryan@example.com");
    testCustomer.setPhoneNumber("+639123456789");
    testCustomer.setStatus("ACTIVE");
    testCustomer.setKycStatus(KycStatus.VERIFIED);
}

   @Test
void createCustomer_success() {

    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE", "12345678"
    );

    CustomerResponse response = new CustomerResponse(
        testCustomer.getId(),
        testCustomer.getFirstName(),
        testCustomer.getLastName(),
        testCustomer.getStatus(),
        testCustomer.getEmail(),
        testCustomer.getCustomerNumber()
    );

    // Mapper: Request -> Entity
    when(customerMapper.toEntity(request))
        .thenReturn(testCustomer);

    // Repository checks
    when(customerRepository.existsByEmail("ryan@example.com"))
        .thenReturn(false);

    when(customerRepository.existsByCustomerNumber("CUST-1000001"))
        .thenReturn(false);

    // Repository save
    when(customerRepository.save(testCustomer))
        .thenReturn(testCustomer);

    // Mapper: Entity -> Response
    when(customerMapper.toResponse(testCustomer))
        .thenReturn(response);

    // When
    CustomerResponse createdCustomer =
        customerService.createCustomer(request);

    // Then
    assertNotNull(createdCustomer);
    assertEquals("CUST-1000001", createdCustomer.customerNumber());
    assertEquals("ryan@example.com", createdCustomer.email());

    verify(customerRepository).save(testCustomer);
}

    @Test
    void createCustomer_duplicateEmail() {
        // Given
        when(customerRepository.existsByEmail("ryan@example.com")).thenReturn(true);

        // When
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(testCustomerRequest));
    }

    @Test
    void createCustomer_duplicateCustomerNumber() {
        // Given
        when(customerRepository.existsByCustomerNumber("CUST-1000001")).thenReturn(true);

        // When
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(testCustomerRequest));
    }

    @Test
void getCustomerById_success() {

    UUID customerId = testCustomer.getId();

    CustomerResponse response = new CustomerResponse(
        customerId,
        "Ryan",
        "Bondoc",
        "ACTIVE",
        "ryan@example.com",
        "CUST-1000001"
    );

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.of(testCustomer));

    when(customerMapper.toResponse(testCustomer))
        .thenReturn(response);

    CustomerResponse customer =
        customerService.getCustomerById(customerId);

    assertNotNull(customer);
    assertEquals(customerId, customer.id());
    assertEquals("CUST-1000001", customer.customerNumber());
    assertEquals("ryan@example.com", customer.email());
}

    @Test
void getCustomerById_notFound() {
    // Given
    UUID customerId = UUID.randomUUID();

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.empty());

    // When / Then
    assertThrows(
        CustomerNotFoundException.class,
        () -> customerService.getCustomerById(customerId)
    );
}

   @Test
void updateCustomer_success() {
    // Given
    when(customerRepository.findByCustomerNumber("CUST-1000001"))
        .thenReturn(Optional.of(testCustomer));

    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan2@example.com",
        "ACTIVE", "12345678"
    );

    Customer savedCustomer = testCustomer;

    when(customerRepository.save(testCustomer))
        .thenReturn(savedCustomer);

    // When
    CustomerResponse updatedCustomer =
        customerService.updateCustomer("CUST-1000001", request);

    // Then
    assertNotNull(updatedCustomer);
    assertEquals("ryan2@example.com", updatedCustomer.email());

    verify(customerRepository).findByCustomerNumber("CUST-1000001");
    verify(customerRepository).save(testCustomer);
}

    @Test
void updateCustomer_emailAfterKycRejection() {
    // Given: KYC status is REJECTED, so email can be changed
    Customer existingCustomer = new Customer();

    existingCustomer.setCustomerNumber("CUST-1000001");
    existingCustomer.setFirstName("Ryan");
    existingCustomer.setLastName("Bondoc");
    existingCustomer.setEmail("ryan@example.com");
    existingCustomer.setPhoneNumber("+639123456789");
    existingCustomer.setStatus("ACTIVE");
    existingCustomer.setKycStatus(KycStatus.REJECTED);

    when(customerRepository.findByCustomerNumber("CUST-1000001"))
        .thenReturn(Optional.of(existingCustomer));

    when(customerRepository.save(existingCustomer))
        .thenReturn(existingCustomer);

    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan2@example.com",
        "ACTIVE", "12345678"
    );

    // When
    CustomerResponse updatedCustomer =
        customerService.updateCustomer("CUST-1000001", request);

    // Then
    assertNotNull(updatedCustomer);
    assertEquals("ryan2@example.com", updatedCustomer.email());

    verify(customerRepository).findByCustomerNumber("CUST-1000001");
    verify(customerRepository).save(existingCustomer);
}
}*/
