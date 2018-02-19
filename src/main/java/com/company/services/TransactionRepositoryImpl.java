package com.company.services;

import com.company.store.Transaction;
import com.company.store.TransactionStore;

/**
 * Created by eThaD on 18.02.2018.
 */
public class TransactionRepositoryImpl implements TransactionRepository {
    private TransactionStore transactionStore;

    public TransactionRepositoryImpl(TransactionStore transactionStore) {
        this.transactionStore = transactionStore;
    }

    @Override
    public int LogTransaction(String account, int sum) {
        return transactionStore.addTransaction(new Transaction(account, sum));
    }

    @Override
    public void FailTransaction(int id) {
        Transaction transaction = transactionStore.getTransaction(id);
        if (transaction != null) {
            transaction.Fail();
            transactionStore.updateTransaction(transaction);
        }
    }

    @Override
    public void SucceedTransaction(int id) {
        Transaction transaction = transactionStore.getTransaction(id);
        if (transaction != null) {
            transaction.Succeed();
            transactionStore.updateTransaction(transaction);
        }
    }
}
