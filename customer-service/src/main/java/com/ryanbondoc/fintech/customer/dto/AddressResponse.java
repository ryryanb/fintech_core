package com.ryanbondoc.fintech.customer.dto;

import java.util.UUID;

public record AddressResponse(
    UUID id,
    String street,
    String city,
    String postalCode
) {
}
