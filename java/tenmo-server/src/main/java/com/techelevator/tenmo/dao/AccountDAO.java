package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDAO {

    BigDecimal GetAccountBalance(int userId);

    void SendTransactionSend(int sendID,int receiveID, BigDecimal value);

    List GetTransferByAccountId(int accountId);
}
