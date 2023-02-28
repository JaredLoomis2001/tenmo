package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public Account findAccountByUserId(int user_id) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if(results.next()){
            account = mapToRow(results);
        }
        return account;
    }

    @Override
    public BigDecimal viewBalance(int account_id) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,account_id);
        while (result.next()){
            balance = result.getBigDecimal("balance");
        }

        return balance;
    }

    @Override
    public void transferFunds(Transfer transfer) {
        BigDecimal fromBalance = new BigDecimal(0);
        BigDecimal toBalance = new BigDecimal(0);
        BigDecimal transferAmount = new BigDecimal(String.valueOf(transfer.getAmount()));

        String sqlTransferSelectFrom = "SELECT * FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlTransferSelectFrom, transfer.getAccount_from());
        while (result.next()){
            fromBalance = new BigDecimal(String.valueOf(result.getBigDecimal("balance")));
        }


        String sqlTransferSelectTo = "SELECT * FROM account WHERE account_id = ?";
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sqlTransferSelectTo, transfer.getAccount_to());
        while (result2.next()){
            toBalance = new BigDecimal(String.valueOf(result2.getBigDecimal("balance")));
        }


        fromBalance = fromBalance.subtract(transferAmount);
        toBalance = toBalance.add(transferAmount);

        String sqlTransferFrom = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sqlTransferFrom, fromBalance, transfer.getAccount_from());

        String sqlTransferTo = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sqlTransferTo, toBalance, transfer.getAccount_to());

        String sqlUpdateStatus = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
        jdbcTemplate.update(sqlUpdateStatus, transfer.getTransfer_id());


    }

    private Account mapToRow (SqlRowSet results){
        Account account = new Account();
        account.setAccount_id(results.getInt("account_id"));
        account.setUser_id(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));

        return account;
    }



}
