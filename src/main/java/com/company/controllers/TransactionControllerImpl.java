package com.company.controllers;

import com.company.dto.OneAccountTransaction;
import com.company.dto.Transaction;
import com.company.services.Status;
import com.company.services.TransactionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

import java.util.function.BiFunction;

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

        if (transaction.getFrom() == null || transaction.getTo() == null) {
            response.status(400);
            return "From or To accounts are not provided";
        }
        if (transaction.getAmount() <= 0) {
            response.status(400);
            return "Amount to transfer is incorrect";
        }

        Status status = transactionService.transferMoney(transaction.getFrom(), transaction.getTo(), transaction.getAmount());
        response.status(mapInternalStatusToHttpStatus(status));
        return "";
    }

    @Override
    public Object withdraw(Request request, Response response) {
        return oneAccountTransfer(request, response, (account, amount) -> (transactionService.withdraw(account, amount)));
    }

    @Override
    public Object topUp(Request request, Response response) {
        return oneAccountTransfer(request, response, (account, amount) -> (transactionService.topUp(account, amount)));
    }

    private Object oneAccountTransfer(Request request, Response response, BiFunction<String, Integer, Status> operation) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        OneAccountTransaction transaction = gson.fromJson(request.body(), OneAccountTransaction.class);

        if (transaction.getAccount() == null) {
            response.status(400);
            return "No account provided";
        }
        if (transaction.getAmount() <= 0) {
            response.status(400);
            return "Amount to transfer is incorrect";
        }

        Status status = operation.apply(transaction.getAccount(), transaction.getAmount());
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
