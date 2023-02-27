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

    @Override
    public Transfer createTransfer(int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from) {
        Transfer transfer = new Transfer();

        String sql = "UPDATE INTO transfer (transfer_status_id, transfer_type_id, amount, account_to, account_from)";
        jdbcTemplate.update(sql, transfer_status_id, transfer_type_id,amount,account_to, account_from);

        String sql2 = "SELECT transfer_id FROM transfer ORDER BY transfer_id DESC LIMIT 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql2);
        int transfer_id = 0;

        while (result.next()){
            transfer_id = result.getInt("transfer_id");
        }

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
