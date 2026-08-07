package com.ryanbondoc.fintech.customer.mapper;

import org.springframework.stereotype.Component;

import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.entity.Address;

@Component
public class AddressMapper {
    public AddressResponse toResponse(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getStreet(),
            address.getCity(),
            address.getPostalCode()
        );
    }

    public Address toEntity(AddressRequest request) {
        Address address = new Address();
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setPostalCode(request.postalCode());
        return address;
    }
}
