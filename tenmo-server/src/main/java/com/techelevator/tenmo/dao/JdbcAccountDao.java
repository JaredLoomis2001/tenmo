package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}
    public BigDecimal getBalance (User user) {
        return null;
    }

//    public BigDecimal deposit (User user , BigDecimal depositValue) {
//        return null;
//    }
//
//    public BigDecimal withdraw (User user , BigDecimal withdrawal) {
//        return null;
//    }
}
