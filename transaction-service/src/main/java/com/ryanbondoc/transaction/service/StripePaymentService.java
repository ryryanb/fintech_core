package com.ryanbondoc.transaction.service;

import java.math.BigDecimal;
import org.springframework.context.annotation.Primary;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ryanbondoc.transaction.exceptions.PaymentProcessingException;
import com.ryanbondoc.transaction.model.PaymentRequest;
import com.ryanbondoc.transaction.model.PaymentResponse;
import com.ryanbondoc.transaction.model.PaymentStatusResponse;
import com.ryanbondoc.transaction.model.RefundResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class StripePaymentService implements PaymentGatewayService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public PaymentResponse processPayment(PaymentRequest payReq) {
        PaymentResponse resp = new PaymentResponse();
        try {
            Stripe.apiKey = stripeApiKey;

            // Create a charge request
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", payReq.getAmount().intValue() * 100); // Amount in cents
            chargeParams.put("currency", "usd");
            chargeParams.put("source", payReq.getPaymentMethod());  // paymentMethod is the Stripe token

            Charge charge = Charge.create(chargeParams);

            log.info("Stripe Charge Response: {}", charge.toString());

            // Check charge status
            resp.setStatus("success");
            resp.setAmountProcessed(BigDecimal.valueOf(charge.getAmount()));
            resp.setTransactionId(charge.getId());
            resp.setPaymentMethod(charge.getPaymentMethod());
            resp.setMessage(charge.getDescription());
            
         // Convert to Instant
            Instant instant = Instant.ofEpochSecond(charge.getCreated());

            // Convert Instant to LocalDateTime in the system default time zone
            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            resp.setTransactionCreated(localDateTime);

        } catch (StripeException e) {
            log.error("StripeException during payment processing for PaymentRequest ID: {} - Error: {}",
                    payReq.getPaymentId(), e.getMessage());
            throw new PaymentProcessingException("Payment failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during payment processing for PaymentRequest ID: {} - Error: {}",
                    payReq.getPaymentId(), e.getMessage());
            throw new PaymentProcessingException("Unexpected payment error", e);
        }
        return resp;
    }

    @Override
    public RefundResponse refundPayment(String transactionId, BigDecimal amount) {
        return null;
    }

    @Override
    public boolean validatePaymentMethod(String paymentMethod) {
        return false;
    }

    @Override
    public PaymentStatusResponse getTransactionStatus(String transactionId) {
        return null;
    }
}