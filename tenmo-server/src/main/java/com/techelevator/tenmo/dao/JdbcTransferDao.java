package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> transferHistory(int account_id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "";


        return transferList;
    }

    @Override
    public Transfer viewTransfer(int transfer_id) {
        Transfer transfer = null;
        String sql = "";


        return transfer;
    }

    @Override
    public Transfer createTransfer(int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from) {
        String sql = "";

        Transfer transfer = new Transfer();
        return transfer;
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
