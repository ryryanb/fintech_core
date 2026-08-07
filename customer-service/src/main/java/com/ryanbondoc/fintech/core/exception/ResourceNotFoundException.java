package com.ryanbondoc.fintech.core.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    // Optional: Add error code for more detailed error handling (common in production systems)
    // public CustomerNotFoundException(String message, String errorCode) {
    //     super(message);
    //     this.errorCode = errorCode;
    // }
    
}
