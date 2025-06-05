package com.ryanbondoc.transaction.service;

import com.ryanbondoc.transaction.model.*;

import java.math.BigDecimal;

public interface PaymentGatewayService {

    /**
     * Processes a payment using the specified payment method and returns the transaction result.
     *
     * @param paymentDetails a Map containing payment information like amount, sender, receiver, and payment method.
     * @return a PaymentResponse containing details about the transaction.
     */
    PaymentResponse processPayment(PaymentRequest paymentDetails);

    /**
     * Refunds a payment transaction.
     *
     * @param transactionId the unique identifier of the transaction to be refunded.
     * @param amount the amount to be refunded.
     * @return a RefundResponse indicating the success or failure of the refund.
     */
    RefundResponse refundPayment(String transactionId, BigDecimal amount);

    /**
     * Validates the payment method (e.g., credit card, bank transfer).
     *
     * @param paymentMethod a string representing the payment method (e.g., "credit_card", "bank_transfer").
     * @return a boolean indicating whether the payment method is valid.
     */
    boolean validatePaymentMethod(String paymentMethod);

    /**
     * Get the status of a specific payment transaction.
     *
     * @param transactionId the unique identifier of the transaction to query.
     * @return a PaymentStatusResponse containing the status of the transaction.
     */
    PaymentStatusResponse getTransactionStatus(String transactionId);
}
