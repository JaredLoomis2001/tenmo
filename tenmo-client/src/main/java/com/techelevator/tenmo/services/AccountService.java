package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;


public class AccountService {
    private String baseUrl = "http://localhost:8080/";
    private static AuthenticatedUser currentUser;
    private static AuthenticationService authenticationService;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService (String url) {
       this.baseUrl = url;
    }

    public void setUser (AuthenticatedUser user) {
        currentUser = user;
    }

    public BigDecimal viewCurrentBalance() {
        BigDecimal currentBalance = null;
        Account currentAccount;
        currentAccount = getAccountByUserId(currentUser.getUser().getId());
        HttpEntity<BigDecimal> entity = getEntity();
        try {
            currentBalance =
                    restTemplate.exchange(baseUrl + "user/account/balance/" + currentAccount.getAccount_id(), HttpMethod.GET, entity, BigDecimal.class).getBody();
        } catch (HttpClientErrorException e) {
            BasicLogger.log(e.getCause() + " " + e.getMessage());
        }
        return currentBalance;
    }

    public Account getAccountByUserId (int user_id) {
        Account account = null;
        HttpEntity<Account> entity = getEntity();
        account = restTemplate.exchange(baseUrl + "user/account/" + user_id, HttpMethod.GET, entity, Account.class).getBody();

        return account;
    }

    private static HttpEntity getEntity() {
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity entity = new HttpEntity<>(header);
        return entity;
    }

}
