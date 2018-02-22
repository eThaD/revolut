package com.company.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryAccountStoreTest {

    @Test
    public void insertAccount_doesNotOverrideAccount_ifKeyAlreadyInStore() {
        InMemoryAccountStore sut = new InMemoryAccountStore();

        boolean res = sut.insertAccount("a", new Account("a", 55));
        assertTrue(res);
        res = sut.insertAccount("a", new Account("a", 88));
        assertFalse(res);

        Account account = sut.getAccount("a");

        assertEquals(55, account.getBalance());
    }
}
