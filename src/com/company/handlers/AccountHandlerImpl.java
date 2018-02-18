package com.company.handlers;

import com.company.services.AccountService;
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
        return "success";
    }
}
