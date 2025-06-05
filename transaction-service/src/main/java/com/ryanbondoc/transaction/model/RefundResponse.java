package com.ryanbondoc.transaction.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundResponse {

    private String transactionId;
    private boolean success;
    private String message;


}
