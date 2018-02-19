package com.company.store;

/**
 * Created by eThaD on 18.02.2018.
 */
public class Transaction {
    private int id;
    private String account;
    private int delta;
    private TransactionStatus status;

    public Transaction(String account, int delta) {
        this.account = account;
        this.delta = delta;
        this.status = TransactionStatus.Pending;
    }

    public void Fail() {
        this.status = TransactionStatus.Failed;
    }

    public void Succeed() {
        this.status = TransactionStatus.Succeeded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}