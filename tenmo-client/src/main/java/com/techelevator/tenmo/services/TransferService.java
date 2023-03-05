package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {
    private String baseUrl = null;
    private static AuthenticatedUser currentUser;
    private static AuthenticationService authenticationService;
    private final RestTemplate restTemplate = new RestTemplate();
    private ConsoleService consoleService = new ConsoleService();
    private Scanner scanner = new Scanner(System.in);

    AccountService accountService = new AccountService(baseUrl);

    public TransferService(String url) {
        baseUrl = url;
    }

    public void setUser(AuthenticatedUser user) {
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

    public void displayTransfers(HttpEntity entity, Transfer[] transferArr) {

        String[] transfer = new String[transferArr.length];
        String[] userAccountFrom = new String[transferArr.length];
        String[] userAccountTo = new String[transferArr.length];
        String[] transferType = new String[transferArr.length];
        String[] transferStatus = new String[transferArr.length];


        System.out.println("Transfers");
        System.out.println("ID            Account From            Account To            Amount");
        System.out.println("-------------------------------------------------------------------");

        for (int i = 0; i < transferArr.length; i++) {

            if (transferArr[i].getAccount_from() == currentUser.getUser().getId()) {
                HttpEntity userEntity = getEntity();
                User recipient = restTemplate.exchange(baseUrl + "/user/transfer" + transferArr[i].getAccount_from(), HttpMethod.GET, userEntity, User.class).getBody();

                String transferred = transferArr[i].toString(recipient);
                transfer[i] = transferred;

                userAccountFrom[i] = currentUser.getUser().getUsername();
                userAccountTo[i] = recipient.getUsername();

                getTransferDetails(transferArr, transfer, transferType, transferStatus, i, transferred);

            }

            if (transferArr[i].getAccount_to() == currentUser.getUser().getId()) {
                HttpEntity userEntity = getEntity();
                User sender = restTemplate.exchange(baseUrl + "/user/transfer" + transferArr[i].getAccount_from(), HttpMethod.GET, userEntity, User.class).getBody();
                String transferred = transferArr[i].toString(sender);
                transfer[i] = transferred;

                userAccountFrom[i] = sender.getUsername();
                userAccountTo[i] = currentUser.getUser().getUsername();

                getTransferDetails(transferArr, transfer, transferType, transferStatus, i, transferred);
            }
        }

        /*
        String choice = String.valueOf(consoleService.promptForInt(String.valueOf(transfer)));
        if (choice.equals("0")) {
            return;
        }
        String[] choiceArr = choice.split("\\t");
        int choiceID = Integer.parseInt(choiceArr[0]);

        Transfer transferDetails = restTemplate.exchange(baseUrl + "/user/transfer/id/" + choiceID, HttpMethod.GET, entity, Transfer.class).getBody();

         */


        for (int i = 0; i < transferArr.length; i++) {
           // if (transferArr[i].getTransfer_id() == choiceID) {
                System.out.println("Transfer: " + transferArr[i].getTransfer_id());
                System.out.println("---------------------------------------------");
                System.out.println("ID: " + transferArr[i].getTransfer_id());
                System.out.println("From: " + userAccountFrom[i]);
                System.out.println("To: " + userAccountTo[i]);
                System.out.println("Type: " + transferType[i]);
                System.out.println("Status: " + transferStatus[i]);
                System.out.println("Amount: $" + new BigDecimal(String.valueOf(transferArr[i].getAmount())));

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

    public void transferMoney(int id , BigDecimal amountSent) {
        //Need searchAccountByUsername
        Account accountFrom;
        Account accountTo;
        User user;

        accountTo = getAccountByAccountId(id);

        user = currentUser.getUser();
        accountFrom = getAccountByUserId(user.getId());

        if (accountTo == accountFrom) {
            System.out.println("Cannot Send Money to Yourself");
            return;
        }

        if (accountFrom.getBalance().compareTo(amountSent) < 0) {
            System.out.println("Cannot Send More Than You Have");
            return;
        }


        Transfer transferring = new Transfer();
        transferring.setAmount(amountSent);
        transferring.setAccount_to(accountTo.getAccount_id());
        transferring.setAccount_from(accountFrom.getAccount_id());
        transferring.setTransfer_type_id(2);
        transferring.setTransfer_status_id(2);

        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transferring , getEntity().getHeaders());

        restTemplate.exchange(baseUrl + "user/transfer/", HttpMethod.POST , entity , Transfer.class);


        System.out.println("Transfer Status: Approved");
        System.out.println("Your current balance is: " + accountFrom.getBalance().subtract(amountSent));
    }


//    private void newTransfer (Transfer transfer) {
//        HttpEntity<Transfer> entity = getEntity();
//        restTemplate.postForObject(baseUrl + "user/transfer" , HttpMethod.POST , Transfer.class , transfer , entity);
//    }


    public Account getAccountByUserId (int id) {
        Account account = null;
        HttpEntity<Account> entity = getEntity();
        account = restTemplate.exchange(baseUrl + "user/account/id/" + id, HttpMethod.GET, entity, Account.class).getBody();

        return account;
    }

    public Account getAccountByAccountId (int id) {
        Account acc = null;
        HttpEntity<Account> accEnt = getEntity();
        acc = restTemplate.exchange(baseUrl + "user/account/" + id , HttpMethod.GET , accEnt , Account.class).getBody();
        return acc;
    }


    public User getUserById(int id) {
        User user = null;
        HttpEntity<User> userEnt = getEntity();
        user = restTemplate.exchange(baseUrl + "user/id/" + id, HttpMethod.GET , userEnt , User.class).getBody();
        return user;
    }

    public void listAccounts () {
        User user;
        String username;
        Account[] accs = restTemplate.exchange(baseUrl + "user/account" , HttpMethod.GET , getEntity() , Account[].class).getBody();

        System.out.println("USERNAME----------------------------ID");

        for (Account acc : accs) {
            user = getUserById(acc.getUser_id());
            username = user.getUsername();
            System.out.println(username + "                         " + acc.getAccount_id());
        }
    }

    private static HttpEntity getEntity() {
        String token = currentUser.getToken();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(token);

        return new HttpEntity<>(header);
    }
}
