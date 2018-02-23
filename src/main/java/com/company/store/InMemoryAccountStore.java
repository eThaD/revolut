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
    public boolean insertAccount(String accountId, Account account) {
        Account accountInStore =
                this
                        .accounts
                        .compute(
                                accountId,
                                (s, existingAccount) -> (existingAccount == null ? account : existingAccount)
                        );

        return accountInStore == account;
    }

    @Override
    public Account getAccount(String accountId) {
        return this.accounts.get(accountId);
    }
}
