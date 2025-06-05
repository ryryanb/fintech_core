package com.ryanbondoc.transaction.controller;


import com.ryanbondoc.transaction.service.PayPalCheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PayPalController {

    private final PayPalCheckoutService payPalCheckoutService;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam String currency, @RequestParam String amount) throws Exception {
        return payPalCheckoutService.createOrder(currency, amount);
    }

    @PostMapping("/capture-order")
    public String captureOrder(@RequestParam String orderId) throws Exception {
        return payPalCheckoutService.captureOrder(orderId).status();
    }
}
