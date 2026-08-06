package com.ryanbondoc.fintech.customer.dto;

public record AddressRequest(
    String street,
    String city,
    String postalCode
) {
}
