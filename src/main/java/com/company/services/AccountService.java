package com.company.services;

import com.company.store.Account;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface AccountService {
    String createAccount();
    AccountSnapshot getAccountSnapshot(String id);
}
