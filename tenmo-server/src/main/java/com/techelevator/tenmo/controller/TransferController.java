package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class TransferController {

    //Our standard controller. The noted out methods were created with the optional assignments in mind
    //I.E the update transfer method would be for the Accepted/Denied status of money transfers
    TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/user/transfer/{id}", method = RequestMethod.GET)
    public Transfer viewTransferByTransferId(@PathVariable("id") int transfer_id){
        return transferDao.viewTransferByTransferId(transfer_id);
    }

    @RequestMapping(path = "/user/transfer/account/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory (@PathVariable int id){
        return transferDao.transferHistory(id);
    }

//    @RequestMapping(path = "/user/transfer/{id}", method = RequestMethod.PUT)
//    public void updateTransfer(@RequestBody int id){
//        transferDao.updateTransfer(id);
//    }

    @RequestMapping(path = "/user/transfer/", method = RequestMethod.POST)
    public void transferMoney(@RequestBody Transfer transfer){
        transferDao.transferFunds(transfer);
    }

}
