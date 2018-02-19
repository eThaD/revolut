package com.company.services;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface TransactionService {
    void transferMoney(String from, String to, int amount);
    void topUp(String account, int amount);
    void withdraw(String account, int amount);
}
