package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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
        Account currentAccount;
        currentAccount = getAccountByUserId(currentUser.getUser().getId());
        Transfer[] transferArr = restTemplate.exchange(baseUrl + "/user/transfer/account/" + currentAccount.getAccount_id(), HttpMethod.GET, getEntity(), Transfer[].class).getBody();
        displayTransfers(entity, transferArr);
        return transferArr;
    }

    public void displayTransfers(HttpEntity entity, Transfer[] transferArr){
        String[] transfer = new String[transferArr.length];
        String[] userAccountFrom = new String[transferArr.length];
        String [] userAccountTo = new String[transferArr.length];
        String[] transferType = new String[transferArr.length];
        String [] transferStatus = new String[transferArr.length];


        System.out.println("Transfers");
        System.out.println("ID            Account From            Account To            Amount");
        System.out.println("-------------------------------------------------------------------");

        for (int i = 0; i < transferArr.length; i++){

            if (transferArr[i].getAccount_from() == currentUser.getUser().getId()){
                HttpEntity userEntity = getEntity();
                User recipient = restTemplate.exchange(baseUrl + "/user" + transferArr[i].getAccount_from(), HttpMethod.GET, userEntity, User.class).getBody();

                String transferred = transferArr[i].toString(recipient);
                transfer[i] = transferred;

                userAccountFrom[i] = currentUser.getUser().getUsername();
                userAccountTo[i] = recipient.getUsername();

                getTransferDetails(transferArr, transfer,transferType, transferStatus, i, transferred);
            }

            if (transferArr[i].getAccount_to() == currentUser.getUser().getId()){
                HttpEntity userEntity = getEntity();
                User sender = restTemplate.exchange(baseUrl + "/user" + transferArr[i].getAccount_from(), HttpMethod.GET, userEntity, User.class).getBody();
                String transferred = transferArr[i].toString(sender);
                transfer[i] = transferred;

                userAccountFrom[i] = sender.getUsername();
                userAccountTo[i] = currentUser.getUser().getUsername();

                getTransferDetails(transferArr, transfer, transferType, transferStatus, i, transferred);
            }
        }

    }

    private void getTransferDetails(Transfer[] transferArr, String [] transfer, String[] transferType, String [] transferStatus, int i, String details){

        if (transferArr[i].getTransfer_type_id() == 1){
            transferType[i] = "Request";
        } else {
            transferType[i] = "Send";
        }

        if (transferArr[i].getTransfer_status_id() == 1){
            transferStatus[i] = "Pending";
        } else if (transferArr[i].getTransfer_status_id() == 2){
            transferStatus[i] = "Approved";
        } else {
            transferStatus[i] = "Rejected";
        }
        transfer[i] = details;
    }

    // method to approve transfers
    private void approveTransfer(Transfer transfer){
        if (new BigDecimal(String.valueOf(transfer.getAmount())).compareTo(accountService.viewCurrentBalance()) == 1) {
            System.out.println("You Don't Have Enough Money");
        }
        String token = currentUser.getToken();
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,header);

        Transfer completedTransfer = restTemplate.exchange(baseUrl + "", HttpMethod.PUT, entity, Transfer.class).getBody();
    }

    public boolean sendBucks (String username , BigDecimal amountSent) {
        //Need searchAccountByUsername
        Account accountFrom;
        Account accountTo;
        User user;
        Account tester;

        user = getUserByUsername(username);
        accountTo = getAccountByUserId(user.getId());

        user = currentUser.getUser();
        accountFrom = getAccountByUserId(user.getId());

        if (accountTo == accountFrom) {
            System.out.println("Cannot Send Money to Yourself");
            return false;
        }

        if (accountFrom.getBalance().compareTo(amountSent) < 0) {
            System.out.println("Cannot Send More Than You Have");
            return false;
        }


        accountFrom.setBalance(accountFrom.getBalance().subtract(amountSent));
        accountTo.setBalance(accountTo.getBalance().add(amountSent));

        HttpEntity<Account> entity = getEntity();

        restTemplate.put(baseUrl + "user/account/balance/" + accountFrom.getAccount_id() , Account.class , accountFrom.getBalance() , entity);
        restTemplate.put(baseUrl + "user/account/balance/" + accountTo.getAccount_id() , Account.class , accountTo.getBalance() , entity);

        return true;

    }

    private void newTransfer (Transfer transfer) {
        HttpEntity<Transfer> entity = getEntity();
        restTemplate.postForObject(baseUrl + "user/transfer" , HttpMethod.POST , Transfer.class , transfer , entity);
    }


    public Account getAccountByUserId (int id) {
        Account account = null;
        HttpEntity<Account> entity = getEntity();
        account = restTemplate.exchange(baseUrl + "user/account/" + id, HttpMethod.GET, entity, Account.class).getBody();

        return account;
    }

    public User getUserByUsername (String username) {
        User user = null;
        HttpEntity<User> userEnt = getEntity();
        user = restTemplate.exchange(baseUrl + "user/" + username , HttpMethod.GET , userEnt , User.class).getBody();
        return user;
    }

    private static HttpEntity getEntity() {
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(token);

        HttpEntity entity = new HttpEntity<>(header);
        return entity;
    }
}
