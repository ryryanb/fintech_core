package com.ryanbondoc.transaction.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = "${cors.allowed.domain}")
public class StripeController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${cors.allowed.domain}")
    private String frontendDomain;

    public static class CheckoutRequest {
        private Long amount;
        private String currency;
        private String productName;

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }

            Stripe.apiKey = stripeApiKey;

            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendDomain + "/payment-success")
                .setCancelUrl(frontendDomain + "/payment-cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "usd")
                                .setUnitAmount(request.getAmount())
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(request.getProductName() != null ? request.getProductName() : "Custom Payment")
                                        .build()
                                )
                                .build()
                        )
                        .setQuantity(1L)
                        .build()
                )
                .build();

            Session session = Session.create(params);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("id", session.getId());

            return ResponseEntity.ok(responseData);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorData);
        } catch (StripeException e) {
            Map<String, String> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorData);
        }
    }
} 