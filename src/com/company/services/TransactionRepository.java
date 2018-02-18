package com.company.services;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface TransactionRepository {
    int LogTransaction(String account, int sum);
    void FailTransaction(int id);
    void SucceedTransaction(int id);
}
