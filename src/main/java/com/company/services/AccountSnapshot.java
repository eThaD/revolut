package com.company.services;

import com.company.store.Account;

public class AccountSnapshot {
    private String id;
    private int balance;

    private AccountSnapshot(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public static AccountSnapshot fromAccount(Account account) {
        if (account == null) return null;
        return new AccountSnapshot(account.getId(), account.getBalance());
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }
}
