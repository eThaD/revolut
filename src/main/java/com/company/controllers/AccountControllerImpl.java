package com.company.controllers;

import com.company.dto.Account;
import com.company.services.AccountService;
import com.company.services.AccountSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 18.02.2018.
 */
public class AccountControllerImpl implements AccountController {
    private AccountService accountService;

    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object createAccount(Request request, Response response) {
        return accountService.createAccount();
    }

    @Override
    public Object getAccount(Request req, Response res) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        AccountSnapshot accountSnapshot = accountService.getAccountSnapshot(req.params("id"));
        if (accountSnapshot == null) {
            res.status(404);
            return null;
        }

        String accountJson = gson.toJson(new Account(accountSnapshot.getId(), accountSnapshot.getBalance()));
        return accountJson;
    }
}
