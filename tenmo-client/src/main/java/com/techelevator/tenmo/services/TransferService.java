package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private String baseUrl = null;
    private static AuthenticatedUser currentUser;
    private static AuthenticationService authenticationService;
    private final RestTemplate restTemplate = new RestTemplate();

    AccountService accountService = new AccountService(baseUrl);

    public TransferService (String url) {
        baseUrl = url;
    }

    public void setUser (AuthenticatedUser user) {
        currentUser = user;
    }

    public Transfer[] viewTransferHistory(){
        HttpEntity entity = getEntity();
        Transfer[] transfer = restTemplate.exchange(baseUrl + "users/" + currentUser.getUser().getId() + "/transfer", HttpMethod.GET, getEntity(), Transfer[].class).getBody();
        // insert method that goes through the transfers and displays them
        return transfer;
    }

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
        if (new BigDecimal(String.valueOf(transfer.getAmount())).compareTo(accountService.viewCurrentBalance()) == 1) {
            System.out.println("You Don't Have Enough Money");
        }
    }

    private void sendBucks (Account account , BigDecimal amountSent) {
        //Need searchAccountByUsername

    }


    private HttpEntity<Account> createAccountEntity (Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account , headers);
    }



    private static HttpEntity getEntity() {
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity entity = new HttpEntity<>(header);
        return entity;
    }
}
