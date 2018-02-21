package com.company.controllers;

import com.company.dto.Transaction;
import com.company.services.Status;
import com.company.services.TransactionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

/**
 * Created by eThaD on 19.02.2018.
 */
public class TransactionControllerImpl implements TransactionController {
    private TransactionService transactionService;

    public TransactionControllerImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Object transfer(Request request, Response response) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Transaction transaction = gson.fromJson(request.body(), Transaction.class);

        if (transaction.getFrom() == null && transaction.getTo() == null) {
            response.status(400);
            return "From and To accounts are not provided";
        }
        if (transaction.getAmount() <= 0) {
            response.status(400);
            return "Amount to transfer is incorrect";
        }
        if (transaction.getFrom() == null) {
            Status status = transactionService.topUp(transaction.getTo(), transaction.getAmount());
            response.status(mapInternalStatusToHttpStatus(status));
            return "";
        }
        if (transaction.getTo() == null) {
            Status status = transactionService.withdraw(transaction.getFrom(), transaction.getAmount());
            response.status(mapInternalStatusToHttpStatus(status));
            return "";
        }

        Status status = transactionService.transferMoney(transaction.getFrom(), transaction.getTo(), transaction.getAmount());
        response.status(mapInternalStatusToHttpStatus(status));
        return "";
    }

    private int mapInternalStatusToHttpStatus(Status status) {
        switch (status) {
            case ACCOUNT_NOT_FOUND:
                return 404;
            case SUCCESS:
                return 200;
            case INSUFFICIENT_FUNDS:
                return 400;
            case STORAGE_UNAVAILABLE:
                return 500;
            default:
                return 500;
        }
    }
}
