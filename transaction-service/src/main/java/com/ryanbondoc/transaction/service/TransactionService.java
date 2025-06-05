package com.ryanbondoc.transaction.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanbondoc.transaction.model.PaymentRequest;
import com.ryanbondoc.transaction.model.PaymentResponse;
import com.ryanbondoc.transaction.model.Transaction;
import com.ryanbondoc.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final PaymentGatewayService paymentService;
   // private final PaymentServiceFactory paymentServiceFactory;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, PaymentGatewayService paymentService) {
        
        this.transactionRepository = transactionRepository;
        this.paymentService = paymentService;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        //PaymentGatewayService paymentService = paymentServiceFactory.getPaymentService(paymentRequest.getPaymentService());
        PaymentResponse resp = paymentService.processPayment(paymentRequest);
        return resp;
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
}
