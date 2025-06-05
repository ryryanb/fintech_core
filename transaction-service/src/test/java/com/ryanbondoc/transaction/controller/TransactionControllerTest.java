package com.ryanbondoc.transaction.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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
import com.ryanbondoc.transaction.model.Transaction;
import com.ryanbondoc.transaction.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();

        // Setup Transaction
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId("TXN-123");
        transaction.setAmountProcessed(new BigDecimal("100.00"));
        transaction.setPaymentMethod("CREDIT_CARD");
        transaction.setStatus("SUCCESS");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setFeesApplied(new BigDecimal("2.50"));
        transaction.setTransactionReference("REF-123");
        transaction.setResultOrErrorCode("SUCCESS-001");
    }

    @Test
    void getTransaction_Success() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(1L))
            .thenReturn(Optional.of(transaction));

        // Act & Assert
        mockMvc.perform(get("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.transactionId").value(transaction.getTransactionId()))
                .andExpect(jsonPath("$.status").value(transaction.getStatus()));

        // Verify
        verify(transactionService).getTransactionById(1L);
    }

    @Test
    void getTransaction_NotFound() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/transactions/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        verify(transactionService).getTransactionById(999L);
    }
} 