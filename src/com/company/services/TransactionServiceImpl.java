package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;
import com.company.store.Transaction;
import com.company.store.TransactionStore;

/**
 * Created by eThaD on 18.02.2018.
 */
public class TransactionServiceImpl implements TransactionService {
    private AccountStore accountStore;
    private TransactionStore transactionStore;

    public TransactionServiceImpl(AccountStore accountStore, TransactionStore transactionStore) {
        this.accountStore = accountStore;
        this.transactionStore = transactionStore;
    }

    @Override
    public void transferMoney(String from, String to, int amount) {

    }

    @Override
    public void topUp(String accountId, int amount) {
        int transactionId = transactionStore.addTransaction(new Transaction(accountId, amount));
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            // update transaction to failed status
        }
        // check for negative total balance??
        Account accountWithNewBalance = account.changeBalance(amount);

        accountStore.updateAccount(accountId, accountWithNewBalance);

        // update transaction to succeeded status
    }

    @Override
    public void Withdraw(String account, int amount) {
    }
}
