package com.company.store;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by eThaD on 18.02.2018.
 */
public class Account {
    private String id;
    private int balance;
    private Lock lock;

    public Account(String id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
        this.lock = new ReentrantLock(false);
    }

    public boolean tryGetLock(long timeoutSeconds) throws InterruptedException {
        return this.lock.tryLock(timeoutSeconds, TimeUnit.SECONDS);
    }

    public void unlock() {
        this.lock.unlock();
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void add(int delta) {
        this.balance += delta;
    }

    public void subtract(int delta) {
        this.balance -= delta;
    }
}


