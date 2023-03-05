package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
        Transfer[] transfers = null;
        transfers= restTemplate.exchange(baseUrl + "/user/transfer/account/" + currentUser.getUser().getId(), HttpMethod.GET, entity, Transfer[].class).getBody();

        System.out.println("-------------------------------------------");
        System.out.println("                  Transfers                ");
        System.out.println("ID         From          To          Amount");

        String fromOrTo = "";
        int name = 0;

        for (Transfer t: transfers){
            if (currentUser.getUser().getId() == t.getAccount_from()){
                fromOrTo = "From: ";
                name = currentUser.getUser().getId();
            } else {
                fromOrTo = "To: ";
                name = t.getAccount_to();

            }
            System.out.println(t.getTransfer_id() + "         " + fromOrTo + name + "         " + t.getAmount());
        }
        System.out.println("-------------------------------------------");
        System.out.println("Please enter transfer ID to view details (0 to cancel)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(Integer.parseInt(input) != 0){
            boolean foundTransfer = false;
            for (Transfer t: transfers){
                if (Integer.parseInt(input) == t.getTransfer_id()){
                    Transfer trans = restTemplate.exchange(baseUrl + "/user/transfer/account/" + t.getTransfer_id(), HttpMethod.GET, entity, Transfer.class).getBody();
                    foundTransfer = true;
                    System.out.println("-------------------------------------------");
                    System.out.println("ID: " + t.getTransfer_id());
                    System.out.println("From: " + t.getAccount_from());
                    System.out.println("To: " + t.getAccount_to());
                    System.out.println("Type: " + t.getTransfer_type_id());
                    System.out.println("Status: " + t.getTransfer_status_id());
                    System.out.println("Amount: $" + t.getAmount());
                }
            }
            if (!foundTransfer){
                System.out.println("Not a valid transfer ID");
            }
        }

        return transfers;
    }


    public void transferMoney(int id , BigDecimal amountSent) {
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

        if (amountSent.compareTo(BigDecimal.valueOf(0)) < 1) {
            System.out.println("Please input a non-negative value greater than 0.");
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
