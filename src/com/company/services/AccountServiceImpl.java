package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;

/**
 * Created by eThaD on 18.02.2018.
 */
public class AccountServiceImpl implements AccountService {
    private AccountStore accountStore;

    public AccountServiceImpl(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @Override
    public void createAccount(String id) {
        createAccount(id, 0);
    }

    @Override
    public void createAccount(String id, int initialSum) {
        accountStore.createAccount(id, new Account(id, initialSum));
    }

    @Override
    public Account getAccount(String id) {
        return accountStore.getAccount(id);
    }
}
