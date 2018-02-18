package com.company.store;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface AccountStore {
    boolean createAccount(String accountId, Account account);
    boolean updateAccount(String accountId, Account account);
}
