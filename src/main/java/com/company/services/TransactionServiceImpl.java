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
        Account accountFrom = accountStore.getAccount(from);
        if (accountFrom == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        if (from.equalsIgnoreCase(to)) {
            return Status.SUCCESS;
        }

        Account accountTo = accountStore.getAccount(to);
        if (accountTo == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        // Comparing account ids to always lock them in the same order to prevent dead-locks
        Account firstToLock;
        Account secondToLock;
        if (accountFrom.getId().compareTo(accountTo.getId()) < 0) {
            firstToLock = accountFrom;
            secondToLock = accountTo;
        } else {
            firstToLock = accountTo;
            secondToLock = accountFrom;
        }

        try {
            if (firstToLock.tryGetLock(1)) {
                try {
                    if (secondToLock.tryGetLock(1)) {
                        try {
                            if (accountFrom.getBalance() < amount) {
                                return Status.INSUFFICIENT_FUNDS;
                            }

                            accountFrom.subtract(amount);
                            accountTo.add(amount);

                            return Status.SUCCESS;
                        } finally {
                            secondToLock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    return Status.STORAGE_UNAVAILABLE;
                } finally {
                    firstToLock.unlock();
                }
            }
            return Status.STORAGE_UNAVAILABLE;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        }
    }

    @Override
    public Status topUp(String accountId, int amount) {
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        try {
            if (account.tryGetLock(1)) {
                account.add(amount);
                account.unlock();
                return Status.SUCCESS;
            }
            return Status.STORAGE_UNAVAILABLE;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        }
    }

    @Override
    public Status withdraw(String accountId, int amount) {
        Account account = accountStore.getAccount(accountId);
        if (account == null) {
            return Status.ACCOUNT_NOT_FOUND;
        }

        try {
            if (account.tryGetLock(1)) {
                try {
                    if (account.getBalance() < amount) {
                        return Status.INSUFFICIENT_FUNDS;
                    }
                    account.subtract(amount);
                    return Status.SUCCESS;
                } finally {
                    account.unlock();
                }
            }
            return Status.STORAGE_UNAVAILABLE;
        } catch (InterruptedException e) {
            return Status.STORAGE_UNAVAILABLE;
        }
    }
}
