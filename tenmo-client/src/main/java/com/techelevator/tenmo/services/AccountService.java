package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService (String url) {
        this.baseUrl = url;
    }

    public BigDecimal viewBalance (int account_id) {
        return null;
    }

    public Account getAccountByUserId (int user_id) {
        Account account = null;

        account = restTemplate.getForObject(baseUrl + user_id, Account.class);

        return account;
    }

    private HttpEntity<Account> createAccountEntity (Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account , headers);
    }

}
