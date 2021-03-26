package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    public Long userId;
    public Long accountId;
    //added balance for additional functionality
    public BigDecimal balance;

    public Long getUserId(){
        return userId;
    }
    public Long getAccountId() {
        return accountId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
