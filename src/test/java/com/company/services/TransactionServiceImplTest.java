package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Test
    public void withdraw_returnsError_ifAccountDoesntExist(){
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.withdraw("acc1", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void withdraw_returnsError_ifBalanceIsNotEnough(){
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(new Account("acc1", 100));
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.withdraw("acc1", 200);

        assertEquals(Status.INSUFFICIENT_FUNDS, result);
    }

    @Test
    public void withdraw_withdrawsMoney_ifBalanceIsEnough() {
        AccountStore accountStore = mock(AccountStore.class);
        Account account = new Account("acc1", 100);
        when(accountStore.getAccount("acc1")).thenReturn(account);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.withdraw("acc1", 50);

        assertEquals(Status.SUCCESS, result);
        assertEquals(50, account.getBalance());
    }

    @Test
    //@RepeatedTest(100)
    public void withdraw_returnsError_ifUnableToLockAccount() {
        AccountStore accountStore = mock(AccountStore.class);
        Account account = new Account("acc1", 100);
        when(accountStore.getAccount("acc1")).thenReturn(account);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        try {
            account.tryGetLock(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(() -> {
            Status result = sut.withdraw("acc1", 50);
            assertEquals(Status.STORAGE_UNAVAILABLE, result);
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
