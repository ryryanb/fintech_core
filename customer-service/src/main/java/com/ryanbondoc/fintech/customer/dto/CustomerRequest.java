package com.ryanbondoc.fintech.customer.dto;

public record CustomerRequest(
    String firstName,
    String lastName,
    String email,
    String status,
    String customerNumber
) {
}
