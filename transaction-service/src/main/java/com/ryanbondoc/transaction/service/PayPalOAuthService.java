package com.ryanbondoc.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Base64;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PayPalOAuthService {

    private static final Logger log = LoggerFactory.getLogger(PayPalOAuthService.class);

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    private static final String PAYPAL_OAUTH_URL = "https://api.sandbox.paypal.com/v1/oauth2/token";

    public String getAccessToken() {
        // Create the authorization header using Basic Authentication (Base64 encoded client_id:client_secret)
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        // Prepare the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare the body with the required "grant_type" parameter
        String body = "grant_type=client_credentials";

        // Prepare the HTTP request
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Initialize RestTemplate to make the request
        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request
        try {
            ResponseEntity<String> response = restTemplate.exchange(PAYPAL_OAUTH_URL, HttpMethod.POST, entity, String.class);

            // If the response is successful, parse the access token
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                log.info("Response Body: {}", responseBody);

                // Parse and extract the access token from the response
                // Assuming the response contains a JSON object with access_token
                String accessToken = extractAccessToken(responseBody);
                return accessToken;
            } else {
                log.error("Failed to retrieve access token. HTTP Status: {}", response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            log.error("Error occurred while requesting access token: {}", e.getMessage());
            return null;
        }
    }

    // Method to extract the access token from the response body
    private String extractAccessToken(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            log.error("Error extracting access token: {}", e.getMessage());
            return null;
        }
    }
}
