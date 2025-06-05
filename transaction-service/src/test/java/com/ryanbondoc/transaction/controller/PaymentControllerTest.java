package com.ryanbondoc.transaction.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanbondoc.transaction.model.PaymentRequest;
import com.ryanbondoc.transaction.model.PaymentResponse;
import com.ryanbondoc.transaction.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PaymentController paymentController;

    private ObjectMapper objectMapper;
    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        objectMapper = new ObjectMapper();
        
        // Setup PaymentRequest
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setPaymentMethod("CREDIT_CARD");
        paymentRequest.setSenderAccount("sender123");
        paymentRequest.setReceiverAccount("receiver456");
        paymentRequest.setTimestamp(LocalDateTime.now());
        paymentRequest.setCurrencyType("USD");
        paymentRequest.setPaymentId("PAY-123");

        // Setup PaymentResponse
        paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId("TXN-123");
        paymentResponse.setStatus("SUCCESS");
        paymentResponse.setMessage("Payment processed successfully");
        paymentResponse.setAmountProcessed(new BigDecimal("100.00"));
        paymentResponse.setPaymentMethod("CREDIT_CARD");
        paymentResponse.setTransactionCreated(LocalDateTime.now());
    }

    @Test
    void processPayment_Success() throws Exception {
        // Arrange
        when(transactionService.processPayment(any(PaymentRequest.class)))
            .thenReturn(paymentResponse);

        // Act & Assert
        mockMvc.perform(post("/payment/process-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(paymentResponse.getTransactionId()))
                .andExpect(jsonPath("$.status").value(paymentResponse.getStatus()))
                .andExpect(jsonPath("$.message").value(paymentResponse.getMessage()));

        // Verify
        verify(transactionService).processPayment(any(PaymentRequest.class));
    }

    @Test
    void processPayment_Failure() throws Exception {
        // Arrange
        when(transactionService.processPayment(any(PaymentRequest.class)))
            .thenThrow(new RuntimeException("Payment processing failed"));

        // Act & Assert
        mockMvc.perform(post("/payment/process-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(transactionService).processPayment(any(PaymentRequest.class));
    }
} 