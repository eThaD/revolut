package com.company;

import com.company.dto.Account;
import com.company.dto.OneAccountTransaction;
import com.company.dto.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionalTest {
    @Test
    public void itIsPossibleToTopUpAndWithdrawMoneyFromAccount() {
        Main.main(null);

        String accountId = createAccount();

        Account account = getAccount(accountId);
        assertEquals(accountId, account.getId());
        assertEquals(0, account.getBalance());

        topUp(new OneAccountTransaction(accountId, 100));

        account = getAccount(accountId);
        assertEquals(100, account.getBalance());

        withdraw(new OneAccountTransaction(accountId, 50));
        account = getAccount(accountId);
        assertEquals(50, account.getBalance());
    }

    @Test
    public void itIsPossibleToTransferMoneyBetweenAccounts() {
        Main.main(null);

        String accountId1 = createAccount();
        String accountId2 = createAccount();
        String accountId3 = createAccount();

        topUp(new OneAccountTransaction(accountId1, 100));
        topUp(new OneAccountTransaction(accountId2, 200));
        topUp(new OneAccountTransaction(accountId3, 300));

        submitTransaction(new Transaction(accountId1, accountId2, 50));
        submitTransaction(new Transaction(accountId1, accountId3, 25));
        submitTransaction(new Transaction(accountId2, accountId3, 100));
        submitTransaction(new Transaction(accountId3, accountId1, 1));

        Account account1 = getAccount(accountId1);
        Account account2 = getAccount(accountId2);
        Account account3 = getAccount(accountId3);

        assertEquals(26, account1.getBalance());
        assertEquals(150, account2.getBalance());
        assertEquals(424, account3.getBalance());
    }

    @Test
    public void noLocksUnderTransactionalLoad() {
        Main.main(null);

        String accountId1 = createAccount();
        String accountId2 = createAccount();
        String accountId3 = createAccount();

        topUp(new OneAccountTransaction(accountId1, 100000));
        topUp(new OneAccountTransaction(accountId2, 100000));
        topUp(new OneAccountTransaction(accountId3, 100000));

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                submitTransaction(new Transaction(accountId1, accountId2, 1));
                submitTransaction(new Transaction(accountId1, accountId3, 1));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                submitTransaction(new Transaction(accountId2, accountId1, 2));
                submitTransaction(new Transaction(accountId2, accountId3, 2));
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                submitTransaction(new Transaction(accountId3, accountId1, 3));
                submitTransaction(new Transaction(accountId3, accountId2, 3));
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Account account1 = getAccount(accountId1);
        Account account2 = getAccount(accountId2);
        Account account3 = getAccount(accountId3);

        assertEquals(103000, account1.getBalance());
        assertEquals(100000, account2.getBalance());
        assertEquals(97000, account3.getBalance());
    }

    private void submitTransaction(Transaction transaction) {
        Gson gson = new GsonBuilder().create();
        String transactionJson = gson.toJson(transaction);
        Response resp = post("http://localhost:4567/transactions/", transactionJson);
        assertEquals(200, resp.Status, resp.body);
    }

    private void topUp(OneAccountTransaction transaction) {
        Gson gson = new GsonBuilder().create();
        String transactionJson = gson.toJson(transaction);
        Response resp = post("http://localhost:4567/topUp/", transactionJson);
        assertEquals(200, resp.Status, resp.body);
    }

    private void withdraw(OneAccountTransaction transaction) {
        Gson gson = new GsonBuilder().create();
        String transactionJson = gson.toJson(transaction);
        Response resp = post("http://localhost:4567/withdraw/", transactionJson);
        assertEquals(200, resp.Status, resp.body);
    }

    private String createAccount() {
        Response resp = post("http://localhost:4567/accounts/", null);
        assertEquals(200, resp.Status);
        UUID.fromString(resp.body);
        return resp.body;
    }

    private Account getAccount(String accountId) {
        Response resp = get("http://localhost:4567/accounts/" + accountId);
        assertEquals(200, resp.Status);
        Gson gson = new GsonBuilder().create();
        Account account = gson.fromJson(resp.body, Account.class);
        return account;
    }

    private Response post(String url, String body) {
        return doRequest("POST", url, body);
    }

    private Response get(String url) {
        return doRequest("GET", url, null);
    }

    private Response doRequest(String method, String url, String body) {
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            if (method == "POST") {
                OutputStream os = conn.getOutputStream();
                if (body != null) os.write(body.getBytes());
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String response = br.lines().collect(Collectors.joining());

            return new Response(conn.getResponseCode(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Response {
        private int Status;
        private String body;

        private Response(int status, String body) {
            Status = status;
            this.body = body;
        }
    }
}
