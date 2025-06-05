package com.ryanbondoc.transaction.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusResponse {

    private String transactionId;
    private String status; // e.g., "success", "failed", "pending"
    private String message;


}
