package com.ryanbondoc.fintech.customer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.entity.Address;

class AddressMapperTest {

    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        addressMapper = new AddressMapper();
    }

    @Test
    void shouldMapAddressEntityToResponse() {
        // Arrange
        UUID id = UUID.randomUUID();

        Address address = new Address();
        address.setId(id);
        address.setStreet("123 Main Street");
        address.setCity("Manila");
        address.setPostalCode("1000");

        // Act
        AddressResponse response = addressMapper.toResponse(address);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.street()).isEqualTo("123 Main Street");
        assertThat(response.city()).isEqualTo("Manila");
        assertThat(response.postalCode()).isEqualTo("1000");
    }

    @Test
    void shouldMapAddressRequestToEntity() {
        // Arrange
        AddressRequest request = new AddressRequest(
            "123 Main Street",
            "Manila",
            "1000"
        );

        // Act
        Address address = addressMapper.toEntity(request);

        // Assert
        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo("123 Main Street");
        assertThat(address.getCity()).isEqualTo("Manila");
        assertThat(address.getPostalCode()).isEqualTo("1000");
    }

    @Test
    void shouldMapNullValuesToResponse() {
        // Arrange
        Address address = new Address();
        address.setId(null);
        address.setStreet(null);
        address.setCity(null);
        address.setPostalCode(null);

        // Act
        AddressResponse response = addressMapper.toResponse(address);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isNull();
        assertThat(response.street()).isNull();
        assertThat(response.city()).isNull();
        assertThat(response.postalCode()).isNull();
    }

    @Test
    void shouldMapNullValuesFromRequestToEntity() {
        // Arrange
        AddressRequest request = new AddressRequest(
            null,
            null,
            null
        );

        // Act
        Address address = addressMapper.toEntity(request);

        // Assert
        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isNull();
        assertThat(address.getCity()).isNull();
        assertThat(address.getPostalCode()).isNull();
    }
}