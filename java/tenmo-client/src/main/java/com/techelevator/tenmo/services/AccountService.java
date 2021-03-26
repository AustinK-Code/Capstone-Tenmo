package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.TransferDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final String BASE_SERVICE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String baseUrl) {
        this.BASE_SERVICE_URL = baseUrl + "/account";
    }

    public BigDecimal getBalance(String authToken) {
        HttpEntity<?> entity = new HttpEntity<>(authHeader(authToken));
        ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_SERVICE_URL, HttpMethod.GET, entity, BigDecimal.class);
        return response.getBody();
    }

    private HttpHeaders authHeader(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }


}
