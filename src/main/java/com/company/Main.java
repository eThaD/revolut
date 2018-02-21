package com.company;

import com.company.controllers.AccountController;
import com.company.controllers.AccountControllerImpl;
import com.company.controllers.TransactionController;
import com.company.controllers.TransactionControllerImpl;
import com.company.services.*;
import com.company.store.AccountStore;
import com.company.store.InMemoryAccountStore;
import com.company.utils.UuidProvider;
import com.company.utils.UuidProviderImpl;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        AccountStore accountStore = new InMemoryAccountStore();
        UuidProvider uuidProvider = new UuidProviderImpl();
        AccountService accountService = new AccountServiceImpl(accountStore, uuidProvider);
        AccountController accountController = new AccountControllerImpl(accountService);

        TransactionService transactionService = new TransactionServiceImpl(accountStore);
        TransactionController transactionController = new TransactionControllerImpl(transactionService);

        post("/accounts/", (req, res) -> accountController.createAccount(req, res));
        get("/accounts/:id", (req, res) -> accountController.getAccount(req, res));

        post("/transactions/", (req, res) -> transactionController.transfer(req, res));
    }
}
