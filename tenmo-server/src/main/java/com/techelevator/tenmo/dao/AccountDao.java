package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {
    public Account getAccountByUserId(int user_id);
    public BigDecimal viewBalance(int account_id);
    public void transferFunds(Transfer transfer);

}
