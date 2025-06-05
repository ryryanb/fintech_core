package com.ryanbondoc.transaction.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ryanbondoc.transaction.model.PaymentRequest;
import com.ryanbondoc.transaction.model.PaymentResponse;
import com.ryanbondoc.transaction.model.Transaction;
import com.ryanbondoc.transaction.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentGatewayService paymentGatewayService;

    @InjectMocks
    private TransactionService transactionService;

    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
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

        // Setup Transaction
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId("TXN-123");
        transaction.setAmountProcessed(new BigDecimal("100.00"));
        transaction.setPaymentMethod("CREDIT_CARD");
        transaction.setStatus("SUCCESS");
        transaction.setTimestamp(LocalDateTime.now());
    }

    @Test
    void processPayment_Success() {
        // Arrange
        when(paymentGatewayService.processPayment(any(PaymentRequest.class)))
            .thenReturn(paymentResponse);

        // Act
        PaymentResponse result = transactionService.processPayment(paymentRequest);

        // Assert
        assertNotNull(result);
        assertEquals(paymentResponse.getTransactionId(), result.getTransactionId());
        assertEquals(paymentResponse.getStatus(), result.getStatus());
        assertEquals(paymentResponse.getAmountProcessed(), result.getAmountProcessed());
        
        // Verify
        verify(paymentGatewayService).processPayment(paymentRequest);
    }

    @Test
    void getTransactionById_Success() {
        // Arrange
        when(transactionRepository.findById(1L))
            .thenReturn(Optional.of(transaction));

        // Act
        Optional<Transaction> result = transactionService.getTransactionById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(transaction.getId(), result.get().getId());
        assertEquals(transaction.getTransactionId(), result.get().getTransactionId());
        assertEquals(transaction.getAmountProcessed(), result.get().getAmountProcessed());
        
        // Verify
        verify(transactionRepository).findById(1L);
    }

    @Test
    void getTransactionById_NotFound() {
        // Arrange
        when(transactionRepository.findById(999L))
            .thenReturn(Optional.empty());

        // Act
        Optional<Transaction> result = transactionService.getTransactionById(999L);

        // Assert
        assertTrue(result.isEmpty());
        
        // Verify
        verify(transactionRepository).findById(999L);
    }

    @Test
    void processPayment_PaymentGatewayFailure() {
        // Arrange
        when(paymentGatewayService.processPayment(any(PaymentRequest.class)))
            .thenThrow(new RuntimeException("Payment gateway error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            transactionService.processPayment(paymentRequest);
        });
        
        // Verify
        verify(paymentGatewayService).processPayment(paymentRequest);
    }
} 