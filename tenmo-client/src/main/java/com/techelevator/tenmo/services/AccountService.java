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

    //Creation of all needed instance variables
    private String baseUrl = "http://localhost:8080/";
    private static AuthenticatedUser currentUser;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService (String url) {
       this.baseUrl = url;
    }

    public void setUser (AuthenticatedUser user) {
        currentUser = user;
    }

    //Shows the users current balance and prints it
    public BigDecimal viewCurrentBalance() {
        //Creation of necessary variables
        BigDecimal currentBalance = null;
        Account currentAccount = null;

        //Searches and finds the account linked to the current users ID
        try {
            currentAccount = getAccountByUserId(currentUser.getUser().getId());
        } catch (Exception e) {
            BasicLogger.log(e.getCause() + " " + e.getMessage());
            System.out.println("Error encountered, please check the logs for further details.");
        }

        //Stores the authorization token
        HttpEntity<BigDecimal> entity = getEntity();

        //Attempts to pull the balance associated with the ID of the account gotten earlier in the code.
        try {
            currentBalance =
                    restTemplate.exchange(baseUrl + "user/account/balance/" + currentAccount.getAccount_id(), HttpMethod.GET, entity, BigDecimal.class).getBody();
        } catch (HttpClientErrorException e) {
            BasicLogger.log(e.getCause() + " " + e.getMessage());
            System.out.println("Error occured, please view the log for further information");
        }
        return currentBalance;
    }


    //Helper methods, some copied from other parts of the code
    public Account getAccountByUserId (int user_id) {
        Account account = null;
        HttpEntity<Account> entity = getEntity();
        account = restTemplate.exchange(baseUrl + "user/account/id/" + user_id, HttpMethod.GET, entity, Account.class).getBody();

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
