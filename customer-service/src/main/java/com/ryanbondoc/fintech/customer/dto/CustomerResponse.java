package com.ryanbondoc.fintech.customer.dto;

import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String firstName,
    String lastName,
    String status, 
    String email, 
    String customerNumber
) {
}
