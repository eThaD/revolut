package com.company.store;

/**
 * Created by eThaD on 18.02.2018.
 */
public class Account {
    private String id;
    private int money;

    public Account(String id, int initialSum) {
        this.id = id;
        this.money = initialSum;
    }

    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }
}

