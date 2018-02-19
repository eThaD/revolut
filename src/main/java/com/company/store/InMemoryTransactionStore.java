package com.company.store;

import java.util.HashMap;

/**
 * Created by eThaD on 18.02.2018.
 */
public class InMemoryTransactionStore implements TransactionStore {
    private HashMap<Integer, Transaction> transactions;
    private int sequenceNumber;

    public InMemoryTransactionStore() {
        transactions = new HashMap<>();
    }

    @Override
    public int addTransaction(Transaction transaction) {
        int id = this.sequenceNumber;
        transaction.setId(id);
        this.sequenceNumber++;
        transactions.put(id, transaction);

        return id;
    }

    @Override
    public Transaction getTransaction(int id) {
        return transactions.get(id);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }
}
