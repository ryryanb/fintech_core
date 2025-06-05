package com.ryanbondoc.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ryanbondoc.transaction.model.PaymentRequest;
import com.ryanbondoc.transaction.model.PaymentResponse;
import com.ryanbondoc.transaction.service.TransactionService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	private final TransactionService transactionService;
	
	 @Autowired
	    public PaymentController(TransactionService transactionService) {
	        this.transactionService = transactionService;
	    }

    @PostMapping("/process-payment")
    public ResponseEntity<Object> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = null;   
            response = transactionService.processPayment(paymentRequest);       
        return ResponseEntity.ok(response);
    }
    
    

   

    
}

