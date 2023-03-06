package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> transferHistory(int account_id);
    Transfer viewTransferByTransferId(int transfer_id);
    void transferFunds(Transfer transfer);

}
