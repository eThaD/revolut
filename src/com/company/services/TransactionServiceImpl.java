package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;

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
        int transactionFrom = transactionRepository.LogTransaction(from, -amount);
        int transactionTo = transactionRepository.LogTransaction(to, amount);

        Account accountFrom = accountStore.getAccount(from);
        if (accountFrom == null) {
            transactionRepository.FailTransaction(transactionFrom);
            transactionRepository.FailTransaction(transactionTo);
        }

        Account accountTo = accountStore.getAccount(to);
        if (accountTo == null) {
            transactionRepository.FailTransaction(transactionFrom);
            transactionRepository.FailTransaction(transactionTo);
        }

        Account newFrom = accountFrom.subtract(amount);
        Account newTo = accountTo.add(amount);

        accountStore.updateAccount(from, newFrom);
        transactionRepository.SucceedTransaction(transactionFrom);
        accountStore.updateAccount(to, newTo);
        transactionRepository.SucceedTransaction(transactionTo);
    }

    @Override
    public void topUp(String accountId, int amount) {
        int transactionId = transactionRepository.LogTransaction(accountId, amount);
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            transactionRepository.FailTransaction(transactionId);
        }

        Account accountWithNewBalance = account.add(amount);

        accountStore.updateAccount(accountId, accountWithNewBalance);

        transactionRepository.SucceedTransaction(transactionId);
    }

    @Override
    public void withdraw(String accountId, int amount) {
        int transactionId = transactionRepository.LogTransaction(accountId, -amount);
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            transactionRepository.FailTransaction(transactionId);
        }

        Account accountWithNewBalance = account.subtract(amount);

        accountStore.updateAccount(accountId, accountWithNewBalance);

        transactionRepository.SucceedTransaction(transactionId);
    }
}
