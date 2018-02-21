package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Test
    public void withdraw_returnsError_ifAccountDoesntExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.withdraw("acc1", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void withdraw_returnsError_ifBalanceIsNotEnough() {
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


    @Test
    public void topUp_returnsError_ifAccountDoesntExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.topUp("acc1", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void topUp_addMoney_ifAccountExists() {
        AccountStore accountStore = mock(AccountStore.class);
        Account account = new Account("acc1", 100);
        when(accountStore.getAccount("acc1")).thenReturn(account);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.topUp("acc1", 50);

        assertEquals(Status.SUCCESS, result);
        assertEquals(150, account.getBalance());
    }

    @Test
    public void topUp_returnsError_ifUnableToLockAccount() {
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
            Status result = sut.topUp("acc1", 50);
            assertEquals(Status.STORAGE_UNAVAILABLE, result);
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void transfer_returnsError_ifFromAccountDoesntExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc2", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void transfer_returnsError_ifToAccountDoesntExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(new Account("acc1", 0));
        when(accountStore.getAccount("acc2")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc2", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void transfer_returnsError_ifAccountsAreTheSameButDontExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc1", 200);

        assertEquals(Status.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void transfer_returnsError_ifAccountsAreTheSameAndExist() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(new Account("acc1", 0));
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc1", 200);

        assertEquals(Status.SUCCESS, result);
    }

    @Test
    public void transfer_returnsError_ifUnableToLockFromAccount() {
        AccountStore accountStore = mock(AccountStore.class);
        Account fromAccount = new Account("acc1", 50);
        Account toAccount = new Account("acc2", 75);
        when(accountStore.getAccount("acc1")).thenReturn(fromAccount);
        when(accountStore.getAccount("acc2")).thenReturn(toAccount);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        try {
            fromAccount.tryGetLock(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(() -> {
            Status result = sut.transferMoney("acc1", "acc2", 200);
            assertEquals(Status.STORAGE_UNAVAILABLE, result);
            assertThrows(IllegalMonitorStateException.class, toAccount::unlock);
            assertThrows(IllegalMonitorStateException.class, fromAccount::unlock);
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transfer_returnsError_ifUnableToLockToAccount() {
        AccountStore accountStore = mock(AccountStore.class);
        Account fromAccount = new Account("acc1", 50);
        Account toAccount = new Account("acc2", 75);
        when(accountStore.getAccount("acc1")).thenReturn(fromAccount);
        when(accountStore.getAccount("acc2")).thenReturn(toAccount);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        try {
            toAccount.tryGetLock(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(() -> {
            Status result = sut.transferMoney("acc1", "acc2", 200);
            assertEquals(Status.STORAGE_UNAVAILABLE, result);
            assertThrows(IllegalMonitorStateException.class, toAccount::unlock);
            assertThrows(IllegalMonitorStateException.class, fromAccount::unlock);
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transfer_returnsError_ifFromBalanceIsNotEnough() {
        AccountStore accountStore = mock(AccountStore.class);
        Account fromAccount = new Account("acc1", 50);
        Account toAccount = new Account("acc2", 75);
        when(accountStore.getAccount("acc1")).thenReturn(fromAccount);
        when(accountStore.getAccount("acc2")).thenReturn(toAccount);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc2", 200);
        assertEquals(Status.INSUFFICIENT_FUNDS, result);
        assertThrows(IllegalMonitorStateException.class, toAccount::unlock);
        assertThrows(IllegalMonitorStateException.class, fromAccount::unlock);
    }

    @Test
    public void transfer_returnsSuccess_ifMoneyTransferred() {
        AccountStore accountStore = mock(AccountStore.class);
        Account fromAccount = new Account("acc1", 50);
        Account toAccount = new Account("acc2", 75);
        when(accountStore.getAccount("acc1")).thenReturn(fromAccount);
        when(accountStore.getAccount("acc2")).thenReturn(toAccount);
        TransactionServiceImpl sut = new TransactionServiceImpl(accountStore);

        Status result = sut.transferMoney("acc1", "acc2", 25);
        assertEquals(Status.SUCCESS, result);
        assertThrows(IllegalMonitorStateException.class, toAccount::unlock);
        assertThrows(IllegalMonitorStateException.class, fromAccount::unlock);
        assertEquals(25, fromAccount.getBalance());
        assertEquals(100, toAccount.getBalance());
    }
}