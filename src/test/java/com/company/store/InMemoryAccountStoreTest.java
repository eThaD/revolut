package com.company.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryAccountStoreTest {

    @Test
    public void createAccount_doesNotOverrideAccount_ifKeyAlreadyInStore() {
        InMemoryAccountStore sut = new InMemoryAccountStore();

        sut.createAccount("a", new Account("a", 55));
        sut.createAccount("a", new Account("a", 88));

        Account account = sut.getAccount("a");

        assertEquals(55, account.getBalance());
    }
}
