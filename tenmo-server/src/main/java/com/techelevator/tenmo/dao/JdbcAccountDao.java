package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    //Searches the database for an Account with the matching User ID
    @Override
    public Account getAccountByUserId(int user_id) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if(results.next()){
            account = mapToRow(results);
        }
        return account;
    }

    //Similar function as the previous method, but for Account ID
    @Override
    public Account getAccountByAccountId (int id) {
        Account account = null;
        String sql = "SELECT account_id , user_id , balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql , id);
        while (results.next()) {
            account = mapToRow(results);
        }
        return account;
    }

    //Searches the database for the balance associated with the Account ID
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

    //Finds all the accounts stored within the database for the user to peer through for the transfer money method
    @Override
    public List<Account> list () {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id , balance , user_id FROM account";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            Account acc = mapToRow(results);
            accounts.add(acc);
        }

        return accounts;
    }


    //Helper method to map a SQLRowset to the in-question variable
    private Account mapToRow (SqlRowSet results){
        Account account = new Account();
        account.setAccount_id(results.getInt("account_id"));
        account.setUser_id(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));

        return account;
    }



}
