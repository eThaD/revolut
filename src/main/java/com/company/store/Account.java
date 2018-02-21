package com.company.store;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by eThaD on 18.02.2018.
 */
public class Account {
    private String id;
    private int money;
    private Lock lock;

    public Account(String id, int initialSum) {
        this.id = id;
        this.money = initialSum;
        this.lock = new ReentrantLock();
    }

    public void tryGetLock(long timeoutSeconds) throws InterruptedException {
        this.lock.tryLock(timeoutSeconds, TimeUnit.SECONDS);
    }

    public void unlock() {
        this.lock.unlock();
    }

    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public void add(int delta) {
        this.money =+ delta;
    }

    public void subtract(int delta) {
        this.money =- delta;
    }
}


