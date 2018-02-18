package com.company;

import com.company.handlers.AccountHandler;
import com.company.handlers.AccountHandlerImpl;
import com.company.services.AccountService;
import com.company.services.AccountServiceImpl;
import com.company.store.AccountStore;
import com.company.store.InMemoryAccountStore;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        AccountStore accountStore = new InMemoryAccountStore();
        AccountService accountService = new AccountServiceImpl(accountStore);
        AccountHandler accountHandler = new AccountHandlerImpl(accountService);

        get("/hello", (req, res) -> accountHandler.createAccount(req, res));
    }
}
