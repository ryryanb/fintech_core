package com.ryanbondoc.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentResponse {

    private String transactionId;
    private String status; // e.g., "success", "failed", "pending"
    private String message; // Additional message for transaction status
    private BigDecimal amountProcessed;
    private String paymentMethod;
    
    private LocalDateTime transactionCreated;


}
