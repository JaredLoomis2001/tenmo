package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {
    Account getAccountByUserId(int user_id);
    BigDecimal viewBalance(int account_id);
    void transferFunds(Transfer transfer);

}
