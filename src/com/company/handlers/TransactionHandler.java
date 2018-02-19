package com.company.handlers;

import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 19.02.2018.
 */
public interface TransactionHandler {
    Object transfer(Request request, Response response);
}
