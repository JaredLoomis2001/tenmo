package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {
    public BigDecimal viewBalance(int balance);
    public void transferFunds(Transfer transfer);

}
