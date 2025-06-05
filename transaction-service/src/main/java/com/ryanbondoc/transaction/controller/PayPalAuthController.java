package com.ryanbondoc.transaction.controller;



import com.ryanbondoc.transaction.service.PayPalOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
public class PayPalAuthController {

    private final PayPalOAuthService payPalOAuthService;

    @Autowired
    public PayPalAuthController(PayPalOAuthService payPalOAuthService) {
        this.payPalOAuthService = payPalOAuthService;
    }

    @GetMapping("/token")
    public String getAccessToken() {
        String accessToken = payPalOAuthService.getAccessToken();
        if (accessToken != null) {
            return accessToken;
        } else {
            return "Failed to retrieve access token.";
        }
    }
}
