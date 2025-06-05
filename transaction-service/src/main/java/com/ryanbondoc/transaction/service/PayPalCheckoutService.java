package com.ryanbondoc.transaction.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPalCheckoutService {

    private final PayPalHttpClient payPalClient;
    
    @Value("${paypal.brandName}")
    private String brandName;

    @Value("${paypal.cancelUrl}")
    private String cancelUrl;

    @Value("${paypal.returnUrl}")
    private String returnUrl;

    @Value("${paypal.landingPage}")
    private String landingPage;

    @Value("${paypal.userAction}")
    private String userAction;

    public String createOrder(String currencyCode, String amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext appContext = new ApplicationContext()
                .brandName(brandName)
                .landingPage(landingPage)
                .cancelUrl(cancelUrl)
                .returnUrl(returnUrl)
                .userAction(userAction);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown()
                .currencyCode(currencyCode)
                .value(amount)));

        orderRequest.applicationContext(appContext);
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest()
                .requestBody(orderRequest);

        HttpResponse<Order> response = payPalClient.execute(request);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.result());
       // log.info("PayPal Order response:\n{}", jsonResponse);
        return jsonResponse;
  
        
    }

    public Order captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());
        HttpResponse<Order> response = payPalClient.execute(request);
        return response.result();
    }
}
