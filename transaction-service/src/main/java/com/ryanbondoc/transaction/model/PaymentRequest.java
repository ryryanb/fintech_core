package com.ryanbondoc.transaction.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class PaymentRequest {

    private BigDecimal amount; // Payment amount
    private String senderAccount; // Sender's information (e.g., account, card, or wallet)
    private String receiverAccount; // Receiver's information (e.g., merchant, recipient account)
    private String paymentMethod; // Payment method (e.g., credit card, bank transfer)
    private LocalDateTime timestamp; // Timestamp
    private String currencyType; // Currency type (e.g., USD, EUR)
    private String paymentId; // Unique identifier for the request
    private String paymentService; 

    // Method to generate a unique Payment ID
    private String generatePaymentId() {
        return UUID.randomUUID().toString();
    }
}
