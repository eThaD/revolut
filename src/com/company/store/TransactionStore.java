package com.company.store;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface TransactionStore {
    int addTransaction(Transaction transaction);
    Transaction getTransaction(int id);
    void updateTransaction(Transaction transaction);
}
