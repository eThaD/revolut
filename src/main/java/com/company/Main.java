package com.company;

import com.company.controllers.AccountController;
import com.company.controllers.AccountControllerImpl;
import com.company.controllers.TransactionController;
import com.company.controllers.TransactionControllerImpl;
import com.company.services.*;
import com.company.store.AccountStore;
import com.company.store.InMemoryAccountStore;
import com.company.store.InMemoryTransactionStore;
import com.company.store.TransactionStore;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        AccountStore accountStore = new InMemoryAccountStore();
        AccountService accountService = new AccountServiceImpl(accountStore);
        AccountController accountController = new AccountControllerImpl(accountService);

        TransactionStore transactionStore = new InMemoryTransactionStore();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(transactionStore);
        TransactionService transactionService = new TransactionServiceImpl(accountStore, transactionRepository);
        TransactionController transactionController = new TransactionControllerImpl(transactionService);

        post("/accounts/", (req, res) -> accountController.createAccount(req, res));
        get("/accounts/:id", (req, res) -> accountController.getAccount(req, res));

        post("/transactions/", (req, res) -> transactionController.transfer(req, res));
    }
}