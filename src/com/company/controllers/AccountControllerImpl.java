package com.company.controllers;

import com.company.dto.Account;
import com.company.services.AccountService;
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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Account account = gson.fromJson(request.body(), Account.class);
        if (account.getId() == null) {
            response.status(400);
            return "Account id is not provided";
        }

        accountService.createAccount(account.getId());
        return "";
    }

    @Override
    public Object getAccount(Request req, Response res) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        com.company.store.Account account = accountService.getAccount(req.params("id"));
        if (account == null) {
            res.status(404);
        }

        String accountJson = gson.toJson(new Account(account.getId(), account.getMoney()));
        return accountJson;
    }
}
