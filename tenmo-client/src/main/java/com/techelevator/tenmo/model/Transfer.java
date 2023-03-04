package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transfer {
    private int transfer_id;

    private int transfer_status_id;

    private int transfer_type_id;

    private BigDecimal amount;

    private int account_to;

    private int account_from;

    public Transfer(int transfer_id, int transfer_status_id, int transfer_type_id, BigDecimal amount, int account_to, int account_from) {
        this.transfer_id = transfer_id;
        this.transfer_status_id = transfer_status_id;
        this.transfer_type_id = transfer_type_id;
        this.amount = amount;
        this.account_to = account_to;
        this.account_from = account_from;
    }

    public Transfer(int transferTypeId) {
        this.transfer_type_id = transferTypeId;
    }

    public Transfer() {}

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public String toString(User user) {
        if (user.getId() == this.getAccount_from()){
            return this.transfer_id + "/t" + "From: " + user.getUsername() + "/t$" + new BigDecimal(String.valueOf(this.amount));
        }
        else if (user.getId() == this.getAccount_to()) {
            return this.transfer_id + "/t" + "To: " + user.getUsername() + "/t$ " + new BigDecimal(String.valueOf(this.amount));
        }
        else return "invalid transaction";
    }
}
