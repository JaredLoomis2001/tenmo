package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@PreAuthorize("isAuthenticated()")
@RestController

public class AccountController {
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Account getAccountByUserId(@RequestParam int user_id){
        return accountDao.getAccountByUserId(user_id);
    }

    @RequestMapping(path = "/accounts/balance/", method = RequestMethod.GET)
    public BigDecimal viewBalance(@PathVariable int account_id){
        return accountDao.viewBalance(account_id);
    }

    @RequestMapping(path = "/new-transfer/approved", method = RequestMethod.PUT)
    public void transferMoney(@RequestBody Transfer transfer){
        accountDao.transferFunds(transfer);
    }


}
