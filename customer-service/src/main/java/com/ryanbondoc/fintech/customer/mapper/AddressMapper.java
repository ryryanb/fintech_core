package com.ryanbondoc.fintech.customer.mapper;

import com.ryanbondoc.fintech.customer.dto.AddressRequest;
import com.ryanbondoc.fintech.customer.dto.AddressResponse;
import com.ryanbondoc.fintech.customer.entity.Address;

public class AddressMapper {
    public static AddressResponse toResponse(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getStreet(),
            address.getCity(),
            address.getPostalCode()
        );
    }

    public static Address toEntity(AddressRequest request) {
        Address address = new Address();
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setPostalCode(request.postalCode());
        return address;
    }
}
