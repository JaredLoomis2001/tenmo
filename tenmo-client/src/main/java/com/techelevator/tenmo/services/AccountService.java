package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;


public class AccountService {
    private final String baseUrl;
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

        HttpEntity entity = getEntity();
        BigDecimal currentBalance =
                restTemplate.exchange(baseUrl + "accounts/balance/", HttpMethod.GET, entity, BigDecimal.class).getBody();
        return currentBalance;

        // user id -->
    }

    public Transfer[] viewTransferHistory(){
        HttpEntity entity = getEntity();
        Transfer[] transfer = restTemplate.exchange(baseUrl + "users/" + currentUser.getUser().getId() + "/transfer", HttpMethod.GET, getEntity(), Transfer[].class).getBody();
        // insert method that goes through the transfers and displays them
        return transfer;
    }
    /*
    METHOD TO DISPLAY TRANSFERS (I guess we need various arrays/lists)
    Should display: Transfer ID      Account From       Acount To       Amount
    1. determine who the transfer is from based on current user's id
        if the account from is equal to the current user then the current user us who sent the money
        accountfrom = current user's username
        account to = recipient's username

        ** helper method displace transfer type and details such as pending, approved, rejected
     */

    // method to get transfer type and details
    private void getTransferDetails(Transfer[] transfer){
        String transferType;
        String transferStatus;
        for (int i = 0; i < transfer.length; i++){
            if (transfer[i].getTransfer_type_id() == 1){
                transferType = "Request";
            } else {
                transferType = "Send";
            }

            if (transfer[i].getTransfer_status_id() == 1){
                transferStatus = "Pending";
            } else if (transfer[i].getTransfer_status_id() == 2){
                transferStatus = "Approved";
            } else {
                transferStatus = "Rejected";
            }
        }
    }

    // method to approve transfers
    private void approveTransfer(Transfer transfer){
        if (new BigDecimal(String.valueOf(transfer.getAmount())).compareTo(viewCurrentBalance()) == 1) {
            System.out.println("You Don't Have Enough Money");
        }
    }

    public Account getAccountByUserId (int user_id) {
        Account account = null;

        account = restTemplate.getForObject(baseUrl + user_id, Account.class);

        return account;
    }

    /*
    private HttpEntity<Account> createAccountEntity (Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account , headers);
    }

     */
    
    private static HttpEntity getEntity() {
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity entity = new HttpEntity<>(header);
        return entity;
    }

}
