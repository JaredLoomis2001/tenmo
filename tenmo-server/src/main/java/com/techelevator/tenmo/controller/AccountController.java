package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class AccountController {
    //Basic controller for the account methods

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/user/account/id/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id){
        return accountDao.getAccountByUserId(id);
    }

    @RequestMapping (path = "/user/account/{id}" , method = RequestMethod.GET)
    public Account getAccountByAccountId (@PathVariable int id) {
        return accountDao.getAccountByAccountId(id);
    }

    @RequestMapping(path = "/user/account/balance/{id}", method = RequestMethod.GET)
    public BigDecimal viewBalance(@PathVariable int id){
        return accountDao.viewBalance(id);
    }


    @RequestMapping(path = "/user/account" , method = RequestMethod.GET)
    public List<Account> listAccounts() { return accountDao.list(); }




}
