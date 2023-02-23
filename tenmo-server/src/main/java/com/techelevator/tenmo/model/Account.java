package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private int account_id;

    private BigDecimal balance;

    private int user_id;

    public Account(int user_id, BigDecimal balance) {
        this.user_id = user_id;
        this.balance = balance;
    }

    public Account() {}

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
