package com.company.dto;

public class OneAccountTransaction {
    private String account;
    private int amount;

    public OneAccountTransaction(String account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public int getAmount() {
        return amount;
    }
}
