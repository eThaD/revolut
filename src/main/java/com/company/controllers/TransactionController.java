package com.company.controllers;

import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 19.02.2018.
 */
public interface TransactionController {
    Object transfer(Request request, Response response);
}
