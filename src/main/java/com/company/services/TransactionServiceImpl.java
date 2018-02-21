package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;

/**
 * Created by eThaD on 18.02.2018.
 */
public class TransactionServiceImpl implements TransactionService {
    private AccountStore accountStore;

    public TransactionServiceImpl(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @Override
    public Status transferMoney(String from, String to, int amount) {
        if (from.equalsIgnoreCase(to)) {
            return Status.SUCCESS;
        }

        Account accountFrom = accountStore.getAccount(from);
        if (accountFrom == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        Account accountTo = accountStore.getAccount(to);
        if (accountTo == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        try {
            // Comparing account ids to always lock them in the same order to prevent dead-locks
            if (accountFrom.getId().compareTo(accountTo.getId()) < 0) {
                accountFrom.tryGetLock(1);
                accountTo.tryGetLock(1);
            } else {
                accountTo.tryGetLock(1);
                accountFrom.tryGetLock(1);
            }

            if (accountFrom.getBalance() < amount) {
                return Status.INSUFFICIENT_FUNDS;
            }

            accountFrom.subtract(amount);
            accountTo.add(amount);
            return Status.SUCCESS;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        } finally {
            accountFrom.unlock();
            accountTo.unlock();
        }
    }

    @Override
    public Status topUp(String accountId, int amount) {
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        try {
            account.tryGetLock(1);
            account.add(amount);
            return Status.SUCCESS;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        } finally {
            account.unlock();

        }
    }

    @Override
    public Status withdraw(String accountId, int amount) {
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }


        try {
            account.tryGetLock(1);
            account.subtract(amount);
            return Status.SUCCESS;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        } finally {
            account.unlock();
        }
    }
}
