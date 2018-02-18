package com.company.store;

import java.util.HashMap;

/**
 * Created by eThaD on 18.02.2018.
 */
public class InMemoryAccountStore implements AccountStore {
    private HashMap<String, Account> accounts;

    public InMemoryAccountStore() {
        accounts = new HashMap<>();
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
    public boolean updateAccount(String accountId, Account account) {
        if (this.accounts.get(accountId) == null) return false;

        this.accounts.put(accountId, account);
        return true;
    }
}
