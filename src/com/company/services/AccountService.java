package com.company.services;

import com.company.store.Account;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface AccountService {
    void createAccount(String id);
    void createAccount(String id, int initialSum);
    Account getAccount(String id);
}
