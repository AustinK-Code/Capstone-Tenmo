package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class TransferDTO { private BigDecimal amount;
    private int TransferId;
    private int TransferTypeId;
    private int TransferStatusId;
    private int AccountTo;
    private int AccountFrom;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferId() {
        return TransferId;
    }

    public void setTransferId(int transferId) {
        TransferId = transferId;
    }

    public int getTransferTypeId() {
        return TransferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        TransferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return TransferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        TransferStatusId = transferStatusId;
    }

    public int getAccountTo() {
        return AccountTo;
    }

    public void setAccountTo(int accountTo) {
        AccountTo = accountTo;
    }

    public int getAccountFrom() {
        return AccountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        AccountFrom = accountFrom;
    }

}

