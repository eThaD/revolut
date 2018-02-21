package com.company.services;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface TransactionService {
    Status transferMoney(String from, String to, int amount);
    Status topUp(String account, int amount);
    Status withdraw(String account, int amount);
}
