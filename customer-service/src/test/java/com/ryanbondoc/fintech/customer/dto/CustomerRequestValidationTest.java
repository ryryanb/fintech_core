package com.ryanbondoc.fintech.customer.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CustomerRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    @Test
    void shouldAcceptValidCustomerRequest() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                "ryan@example.com",
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectBlankFirstName() {

        CustomerRequest request = new CustomerRequest(
                "",
                "Bondoc",
                "ryan@example.com",
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectBlankLastName() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "",
                "ryan@example.com",
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectInvalidEmail() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                "invalid-email",
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectMissingEmail() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                null,
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAllowCustomerNumberToBeOmitted() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                "ryan@example.com",
                "ACTIVE",
                null
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectCustomerNumberThatIsTooShort() {

        CustomerRequest request = new CustomerRequest(
                "Ryan",
                "Bondoc",
                "ryan@example.com",
                "ACTIVE",
                "123"
        );

        Set<ConstraintViolation<CustomerRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}