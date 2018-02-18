package com.company.handlers;

import com.company.dto.Account;
import com.company.services.AccountService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 18.02.2018.
 */
public class AccountHandlerImpl implements AccountHandler {
    private AccountService accountService;

    public AccountHandlerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object createAccount(Request request, Response response) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Account account = gson.fromJson(request.body(), Account.class);
        accountService.createAccount(account.getId());

        return "";
    }

    @Override
    public Object getAccount(Request req, Response res) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        com.company.store.Account account = accountService.getAccount(req.params("id"));
        Account accountDto = new Account(account.getId(), account.getMoney());

        String accountJson = gson.toJson(accountDto);
        res.body(accountJson);

        return accountJson;
    }
}
