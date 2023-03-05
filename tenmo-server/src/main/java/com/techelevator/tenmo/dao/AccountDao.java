package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account getAccountByUserId(int user_id);
    BigDecimal viewBalance(int account_id);
    List<Account> list();
    Account getAccountByAccountId(int id);

}
