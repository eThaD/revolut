package com.company.store;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by eThaD on 18.02.2018.
 */
public class InMemoryAccountStore implements AccountStore {
    private ConcurrentHashMap<String, Account> accounts;

    public InMemoryAccountStore() {
        accounts = new ConcurrentHashMap<>();
    }

    @Override
    public boolean createAccount(String accountId, Account account) {
        if (this.accounts.get(accountId) == null) {
            this.accounts.put(accountId, account);
            return true;
        }
        return false;
    }

    @Override
    public Account getAccount(String accountId) {
        return this.accounts.get(accountId);
    }
}
