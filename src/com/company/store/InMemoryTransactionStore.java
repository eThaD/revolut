package com.company.store;

import java.util.ArrayList;

/**
 * Created by eThaD on 18.02.2018.
 */
public class InMemoryTransactionStore implements TransactionStore {
    private ArrayList<Transaction> transactions;

    public InMemoryTransactionStore() {
        transactions = new ArrayList<>();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
