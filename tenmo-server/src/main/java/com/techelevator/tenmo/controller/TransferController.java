package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;

public class TransferController {
    TransferDao transferDao;

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer newTransfer(@RequestBody Transfer transfer){
        return transfer = transferDao.createTransfer(transfer.getTransfer_status_id(), transfer.getTransfer_type_id(), transfer.getAmount(), transfer.getAccount_to(), transfer.getAccount_from());
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Transfer viewTransferByTransferId(@PathVariable("id") int transfer_id){
        return transferDao.viewTransferByTransferId(transfer_id);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory (@PathVariable int account_id){
        return transferDao.transferHistory(account_id);
    }
}
