package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> transferHistory(int account_id);
    public Transfer viewTransfer(int transfer_id);
    public Transfer createTransfer(int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from);

}
