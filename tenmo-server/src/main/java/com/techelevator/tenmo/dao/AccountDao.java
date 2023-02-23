package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance (User user);

//    BigDecimal deposit (User user , BigDecimal depositValue);
//
//    BigDecimal withdraw (User user , BigDecimal withdrawal);


}
