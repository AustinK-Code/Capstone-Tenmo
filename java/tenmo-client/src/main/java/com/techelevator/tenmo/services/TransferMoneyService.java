package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.TransferDTO;
import com.techelevator.tenmo.models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferMoneyService {
    private static final String AUTH_TOKEN = "";
    private final String BASE_SERVICE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferMoneyService(String base_service_url) {
        BASE_SERVICE_URL = base_service_url;
    }

    public User[] listUsers(String token){
        HttpEntity<?> entity = new HttpEntity<>(authHeader(token));
        ResponseEntity<User[]> response = restTemplate.exchange(BASE_SERVICE_URL + "/account/list", HttpMethod.GET, entity, User[].class);
        return response.getBody();
    }


    public void sendBucks(String token ,int userID, TransferDTO amount){
        restTemplate.exchange(BASE_SERVICE_URL+"/send/"+userID, HttpMethod.POST, makeAuthEntity(token,amount),Integer.class);
    }
    private HttpEntity makeAuthEntity(String token,TransferDTO amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(amount,headers);
    }
    private HttpHeaders authHeader(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }
    public TransferDTO[] getTransferHistory(String authToken){
        HttpEntity<?> entity = new HttpEntity<>(authHeader(authToken));
        ResponseEntity<TransferDTO[]> response = restTemplate.exchange(BASE_SERVICE_URL + "/transfers/list", HttpMethod.GET, entity, TransferDTO[].class);
        return response.getBody();
    }
}
