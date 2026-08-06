package com.ryanbondoc.fintech.core.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    
    // Optional: Add error code for more detailed error handling (common in production systems)
    // public BusinessException(String message, String errorCode) {
    //     super(message);
    //     this.errorCode = errorCode;
    // }
}
