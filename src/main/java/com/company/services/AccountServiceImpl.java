package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;
import com.company.utils.UuidProvider;

/**
 * Created by eThaD on 18.02.2018.
 */
public class AccountServiceImpl implements AccountService {
    private AccountStore accountStore;
    private UuidProvider uuidProvider;

    public AccountServiceImpl(AccountStore accountStore, UuidProvider uuidProvider) {
        this.accountStore = accountStore;
        this.uuidProvider = uuidProvider;
    }

    @Override
    public String createAccount() {
        for(int i = 0; i < 10 ; i++) {
            String id = uuidProvider.GenerateUUID();
            if (accountStore.createAccount(id, new Account(id, 0))) return id;
        }
        return null;
    }

    @Override
    public AccountSnapshot getAccountSnapshot(String id) {
        return AccountSnapshot.fromAccount(accountStore.getAccount(id));
    }
}
