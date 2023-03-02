package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class TransferController {
    TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/new-transfer", method = RequestMethod.POST)
    public Transfer newTransfer(@RequestBody Transfer transfer){
        return transfer = transferDao.createTransfer(transfer.getTransfer_status_id(), transfer.getTransfer_type_id(), transfer.getAmount(), transfer.getAccount_to(), transfer.getAccount_from());
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer viewTransferByTransferId(@PathVariable("id") int transfer_id){
        return transferDao.viewTransferByTransferId(transfer_id);
    }

    @RequestMapping(path = "/user/{id}/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory (@PathVariable int account_id){
        return transferDao.transferHistory(account_id);
    }
}
