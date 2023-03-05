package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> transferHistory(int account_id);
    Transfer viewTransferByTransferId(int transfer_id);
    Transfer newTransfer(int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from);
    void transferFunds(Transfer transfer);

}
