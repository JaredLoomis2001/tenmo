package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class AccountService {
    private static AuthenticatedUser currentUser;
    private static ConsoleService console;
    private static AuthenticationService authenticationService;
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String API_BASE_URL = "http://localhost:8080/";

    public AccountService(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    Scanner scanner = new Scanner(System.in);

    AccountService accountService = new AccountService(new AuthenticationService(API_BASE_URL));

    // register screen
    public static void register(){}

    // login in screen
    public static void login(){}

    // ------- BUSINESS LOGIC ------
    // view current balance

    // view transfer history

    // view & select transfers

    // approve transfers

    // get transfer type / status



    private static HttpEntity getEntity(){
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity entity = new HttpEntity<>(header);
        return entity;

    }
}
