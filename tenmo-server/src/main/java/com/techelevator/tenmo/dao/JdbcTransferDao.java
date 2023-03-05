package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> transferHistory(int account_id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_to = ? OR account_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);

        while (results.next()){
            Transfer transfer = mapToRow(results);
            transferList.add(transfer);
        }

        return transferList;
    }

    @Override
    public Transfer viewTransferByTransferId(int transfer_id) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer_id);

        while(result.next()){
            transfer = mapToRow(result);
        }

        return transfer;
    }

//    @Override
//    public Transfer newTransfer(int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from) {
//        Transfer transfer = new Transfer();
//
//        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, amount, account_to, account_from) " +
//                "VALUES (? , ? , ? , ? , ?)";
//
//        jdbcTemplate.update(sql, transfer_status_id, transfer_type_id,amount,account_to, account_from);
//
//        String sql2 = "SELECT transfer_id FROM transfer ORDER BY transfer_id DESC LIMIT 1";
//
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql2);
//
//        while (result.next()) {
//            transfer = mapToRow(result);
//        }
//
//        return transfer;
//    }

//    @Override
//    public void updateTransfer(Transfer transfer) {
//        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, amount, account_to, account_from) " +
//                "VALUES (? , ? , ? , ? , ?)";
//
//        jdbcTemplate.update(sql, transfer.getTransfer_status_id(), transfer.getTransfer_type_id(),transfer.getAmount(), transfer.getAccount_to(), transfer.getAccount_from());
//    }

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

        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, amount, account_to, account_from) " +
                "VALUES (? , ? , ? , ? , ?)";

        jdbcTemplate.update(sql, transfer.getTransfer_status_id(), transfer.getTransfer_type_id(),transfer.getAmount(),transfer.getAccount_to(), transfer.getAccount_from());

//        String sqlUpdateStatus = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
//        jdbcTemplate.update(sqlUpdateStatus, transfer.getTransfer_id());




    }

    private Transfer mapToRow (SqlRowSet results){
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(results.getInt("transfer_id"));
        transfer.setTransfer_status_id(results.getInt("transfer_status_id"));
        transfer.setTransfer_type_id(results.getInt("transfer_type_id"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setAccount_to(results.getInt("account_to"));
        transfer.setAccount_from(results.getInt("account_from"));

        return transfer;
    }
}
