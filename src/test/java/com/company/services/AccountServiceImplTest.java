package com.company.services;

import com.company.store.Account;
import com.company.store.AccountStore;
import com.company.utils.UuidProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by eThaD on 19.02.2018.
 */
public class AccountServiceImplTest {

    // TODO: Make a common setup for all tests

    @Test
    public void getAccount_returnsAccount_ifAccountExistsInStore() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(new Account("acc1", 555));
        UuidProvider uuidProvider = mock(UuidProvider.class);
        AccountServiceImpl sut = new AccountServiceImpl(accountStore, uuidProvider);

        AccountSnapshot account = sut.getAccountSnapshot("acc1");

        assertEquals(account.getId(), "acc1");
        assertEquals(account.getBalance(), 555);
    }

    @Test
    public void getAccount_returnsNull_ifAccountDoesntExistInStore() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccount("acc1")).thenReturn(null);
        UuidProvider uuidProvider = mock(UuidProvider.class);
        AccountServiceImpl sut = new AccountServiceImpl(accountStore, uuidProvider);

        AccountSnapshot account = sut.getAccountSnapshot("acc1");

        assertNull(account);
    }

    @Test
    public void createAccount_createsAccountAndSavesIdInStore() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.createAccount(eq("uuid1"), any(Account.class))).thenReturn(true);

        UuidProvider uuidProvider = mock(UuidProvider.class);
        when(uuidProvider.GenerateUUID()).thenReturn("uuid1");

        AccountServiceImpl sut = new AccountServiceImpl(accountStore, uuidProvider);

        String accountId = sut.createAccount();

        assertEquals("uuid1", accountId);

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountStore).createAccount(eq("uuid1"), argumentCaptor.capture());
        assertEquals("uuid1", argumentCaptor.getValue().getId());
        assertEquals(0, argumentCaptor.getValue().getBalance());
    }

    @Test
    public void createAccount_regeneratesUuidForAccount_ifAccoutWithUuidAlreadyExists() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.createAccount(eq("uuid1"), any(Account.class))).thenReturn(false);
        when(accountStore.createAccount(eq("uuid2"), any(Account.class))).thenReturn(true);

        UuidProvider uuidProvider = mock(UuidProvider.class);
        when(uuidProvider.GenerateUUID()).thenReturn("uuid1").thenReturn("uuid2");

        AccountServiceImpl sut = new AccountServiceImpl(accountStore, uuidProvider);

        String accountId = sut.createAccount();

        assertEquals("uuid2", accountId);

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountStore).createAccount(eq("uuid2"), argumentCaptor.capture());
        assertEquals("uuid2", argumentCaptor.getValue().getId());
        assertEquals(0, argumentCaptor.getValue().getBalance());
    }


    @Test
    public void createAccount_doesNotGoIntoInfiniteLoop_ifStoreAlwaysReturnFalse() {
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.createAccount(anyString(), any(Account.class))).thenReturn(false);

        UuidProvider uuidProvider = mock(UuidProvider.class);
        AccountServiceImpl sut = new AccountServiceImpl(accountStore, uuidProvider);

        String accountId = sut.createAccount();

        assertNull(accountId);
    }
}
