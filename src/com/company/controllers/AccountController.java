package com.company.controllers;

import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface AccountController {
    Object createAccount(Request request, Response response);
    Object getAccount(Request req, Response res);
}
