package com.company.dto;

/**
 * Created by eThaD on 19.02.2018.
 */
public class Account {
    private String id;
    private int balance;

    public Account(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
