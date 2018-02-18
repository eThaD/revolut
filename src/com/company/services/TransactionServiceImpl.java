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
    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountStore accountStore, TransactionRepository transactionRepository) {
        this.accountStore = accountStore;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void transferMoney(String from, String to, int amount) {

    }

    @Override
    public void topUp(String accountId, int amount) {
        int transactionId = transactionRepository.LogTransaction(accountId, amount);
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            transactionRepository.FailTransaction(transactionId);
        }
        // check for negative total balance??
        Account accountWithNewBalance = account.changeBalance(amount);

        accountStore.updateAccount(accountId, accountWithNewBalance);

        transactionRepository.SucceedTransaction(transactionId);
    }

    @Override
    public void withdraw(String account, int amount) {
    }
}
