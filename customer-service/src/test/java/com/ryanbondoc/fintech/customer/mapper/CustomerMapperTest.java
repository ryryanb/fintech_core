/*package com.ryanbondoc.fintech.customer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ryanbondoc.fintech.customer.dto.CustomerRequest;
import com.ryanbondoc.fintech.customer.dto.CustomerResponse;
import com.ryanbondoc.fintech.customer.entity.Customer;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void shouldMapCustomerEntityToResponse() {
        // Given
        UUID id = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("Ryan");
        customer.setLastName("Bondoc");
        customer.setStatus("ACTIVE");

        // When
        CustomerResponse response = customerMapper.toResponse(customer);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.firstName()).isEqualTo("Ryan");
        assertThat(response.lastName()).isEqualTo("Bondoc");
        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    void shouldMapCustomerRequestToEntity() {
        // Given
        CustomerRequest request = new CustomerRequest(
            "Ryan",
            "Bondoc",
            "ryan@example.com",
            "ACTIVE", "12345678"
        );

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("Ryan");
        assertThat(customer.getLastName()).isEqualTo("Bondoc");
        assertThat(customer.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void shouldMapNullValuesToResponse() {
        // Given
        Customer customer = new Customer();
        customer.setId(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setStatus(null);

        // When
        CustomerResponse response = customerMapper.toResponse(customer);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNull();
        assertThat(response.firstName()).isNull();
        assertThat(response.lastName()).isNull();
        assertThat(response.status()).isNull();
    }

    @Test
    void shouldMapNullValuesFromRequestToEntity() {
        // Given
        CustomerRequest request = new CustomerRequest(
            null,
            null,
            null,
            null, null
        );

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isNull();
        assertThat(customer.getLastName()).isNull();
        assertThat(customer.getStatus()).isNull();
    }

    @Test
void shouldMapEmailFromRequestToEntity() {
    // Given
    CustomerRequest request = new CustomerRequest(
        "Ryan",
        "Bondoc",
        "ryan@example.com",
        "ACTIVE", "12345678"
    );

    // When
    Customer customer = customerMapper.toEntity(request);

    // Then
    assertThat(customer.getFirstName()).isEqualTo("Ryan");
    assertThat(customer.getLastName()).isEqualTo("Bondoc");
    assertThat(customer.getEmail()).isEqualTo("ryan@example.com");
    assertThat(customer.getStatus()).isEqualTo("ACTIVE");
}
}*/