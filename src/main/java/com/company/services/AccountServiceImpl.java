package com.company.services;

import java.util.UUID;
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
    public String createAccount() {
        for(;;) {
            String id = UUID.randomUUID().toString();
            if (accountStore.createAccount(id, new Account(id, 0))) return id;
        }
    }

    @Override
    public Account getAccount(String id) {
        return accountStore.getAccount(id);
    }
}
