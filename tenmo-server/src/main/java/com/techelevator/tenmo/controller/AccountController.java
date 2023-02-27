package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

public class AccountController {
    private AccountDao accountDao;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Account getAccountByUserId(@RequestParam int user_id){
        return accountDao.findAccountByUserId(user_id);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public BigDecimal viewBalance(@PathVariable int account_id){
        return accountDao.viewBalance(account_id);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void transferMoney(@RequestBody Transfer transfer){
        accountDao.transferFunds(transfer);
    }


}
