package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;

    private int transfer_status_id;

    private int transfer_type_id;

    private BigDecimal amount;

    private int account_to;
    
    private int account_from;

    public Transfer(int transferTypeId) {
        this.transfer_type_id = transferTypeId;
    }

    public Transfer() {}

    public int getTransferId() {
        return transfer_id;
    }

    public void setTransferId(int transferId) {
        this.transfer_id = transferId;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }
}
