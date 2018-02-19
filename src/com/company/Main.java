package com.company;

import com.company.handlers.AccountHandler;
import com.company.handlers.AccountHandlerImpl;
import com.company.handlers.TransactionHandler;
import com.company.handlers.TransactionHandlerImpl;
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
        AccountHandler accountHandler = new AccountHandlerImpl(accountService);

        TransactionStore transactionStore = new InMemoryTransactionStore();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(transactionStore);
        TransactionService transactionService = new TransactionServiceImpl(accountStore, transactionRepository);
        TransactionHandler transactionHandler = new TransactionHandlerImpl(transactionService);

        post("/accounts/", (req, res) -> accountHandler.createAccount(req, res));
        get("/accounts/:id", (req, res) -> accountHandler.getAccount(req, res));

        post("/transactions/", (req, res) -> transactionHandler.transfer(req, res));
    }
}
