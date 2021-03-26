package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransactionController {
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    public TransactionController(AccountDAO account, UserDAO userDAO){

        this.accountDAO = account;
        this.userDAO = userDAO;
    }


    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal p) throws UsernameNotFoundException {
        int id = userDAO.findIdByUsername(p.getName());
        return accountDAO.GetAccountBalance(id);
    }


    @RequestMapping(path = "/send/{sendToId}", method = RequestMethod.POST)
    public void sendMoney(Principal p, @PathVariable int sendToId, @Valid @RequestBody TransferDTO amount){
        //turn the Principal into a user and get the id number
        User user = userDAO.findByUsername(p.getName());
        int id = userDAO.findIdByUsername(p.getName());

        if(getBalance(p).compareTo(amount.getAmount()) >= 0){//checks is there is enough money in the account
        accountDAO.SendTransactionSend(id,sendToId,amount.getAmount());}
        else {
            System.out.println("Transaction failed");
        }
    }
    @RequestMapping(path = "/account/list", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDAO.findAll();
    }

    @RequestMapping(path = "/transfers/list", method = RequestMethod.GET)
    public List<TransferDTO> transferListById(Principal p){
    int id = userDAO.findIdByUsername(p.getName());
    int accountId = userDAO.findAccountIdByUserId(id);
    return accountDAO.GetTransferByAccountId(accountId);
    }


    private Long getCurrentUserId(Principal principal) {
        return userDAO.findByUsername(principal.getName()).getId();
    }



}
