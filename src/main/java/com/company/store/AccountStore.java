package com.company.store;

/**
 * Created by eThaD on 18.02.2018.
 */
public interface AccountStore {
    boolean insertAccount(String accountId, Account account);

    Account getAccount(String accountId);
}
