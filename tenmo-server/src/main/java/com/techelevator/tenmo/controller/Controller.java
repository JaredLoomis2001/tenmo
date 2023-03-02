package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public class Controller {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;


    public Controller(AccountDao accountDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> list(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public User findUserById(@RequestParam int id){
        User user = userDao.getUserById(id);
        return user;
    }

    @RequestMapping(path = "/user/username", method = RequestMethod.GET)
    public User findUserByUsername(@RequestParam String name){
        User user = userDao.findByUsername(name);
        return user;
    }

    @RequestMapping(path = "/user/{id}/username", method = RequestMethod.GET)
    public int findIdByUsername(@RequestParam String username){
        int id = userDao.findIdByUsername(username);
        return id;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Account getAccountByUserId(@RequestParam int user_id){
        return accountDao.getAccountByUserId(user_id);
    }

    @RequestMapping(path = "/users/{id}/balance", method = RequestMethod.GET)
    public BigDecimal viewBalance(@PathVariable int account_id){
        return accountDao.viewBalance(account_id);
    }

    @RequestMapping(path = "/new-transfer/approved", method = RequestMethod.PUT)
    public void transferMoney(@RequestBody Transfer transfer){
        accountDao.transferFunds(transfer);
    }

    @RequestMapping(path = "/new-transfer", method = RequestMethod.POST)
    public Transfer newTransfer(@RequestBody Transfer transfer){
        return transfer = transferDao.createTransfer(transfer.getTransfer_status_id(), transfer.getTransfer_type_id(), transfer.getAmount(), transfer.getAccount_to(), transfer.getAccount_from());
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer viewTransferByTransferId(@PathVariable("id") int transfer_id){
        return transferDao.viewTransferByTransferId(transfer_id);
    }

    @RequestMapping(path = "/user/{id}/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory (@PathVariable int account_id){
        return transferDao.transferHistory(account_id);
    }


}
