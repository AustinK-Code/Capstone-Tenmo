package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.sun.el.lang.ELArithmetic.add;
import static com.sun.el.lang.ELArithmetic.subtract;



@Component
public class JdbcAccountDAO implements AccountDAO{
    private JdbcTemplate jdbcTemplate;
    private UserDAO userDAO;


    public JdbcAccountDAO(JdbcTemplate jdbcTemplate, UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }


    public BigDecimal GetAccountBalance(int userId) {
    String sql = "SELECT user_id, balance, account_id FROM accounts WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()){
            Account a = mapRowToAccount(rowSet);
            return a.getBalance();
        }

       return null;
    }
    //sends a send request to transaction table
    public void SendTransactionSend(int sendID,int receiveID, BigDecimal value){
        int sendAccountId = userDAO.findAccountIdByUserId(sendID);
        int receiveAccountId = userDAO.findAccountIdByUserId(receiveID);
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to,amount) VALUES (2,2,?,?,?)";
        jdbcTemplate.update(sql, sendAccountId, receiveAccountId ,value);
        String sqlRemove ="UPDATE accounts SET balance = ? WHERE user_id = ?;";//updates account of user sending money
        jdbcTemplate.update(sqlRemove, subtract(GetAccountBalance(sendID), value), sendID);
        String sqlAdd ="UPDATE accounts SET balance = ? WHERE user_id = ?;";//updates account of user receiving money
        jdbcTemplate.update(sqlAdd, add(GetAccountBalance(receiveID), value), receiveID);
    }

    public List GetTransferByAccountId(int accountId){
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_to = ? OR account_from = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(rowSet.next()){
            TransferDTO t = mapRowToTransfer(rowSet);
            transferList.add(t);
        }
        return transferList;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setUserId(rs.getLong("user_id"));
        account.setAccountId(rs.getLong("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

    private TransferDTO mapRowToTransfer(SqlRowSet rs) {
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        return transfer;
    }
}
