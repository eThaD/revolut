package com.company.handlers;

import com.company.dto.Transaction;
import com.company.services.TransactionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

/**
 * Created by eThaD on 19.02.2018.
 */
public class TransactionHandlerImpl implements TransactionHandler {
    private TransactionService transactionService;

    public TransactionHandlerImpl(TransactionService transactionService) {
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
            transactionService.topUp(transaction.getTo(), transaction.getAmount());
            return "";
        }
        if (transaction.getTo() == null) {
            transactionService.withdraw(transaction.getFrom(), transaction.getAmount());
            return "";
        }

        transactionService.transferMoney(transaction.getFrom(), transaction.getTo(), transaction.getAmount());
        return "";
    }
}
