package com.company;

import com.company.dto.Account;
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
    public void CreateAccountAndTopUp() {
        Main.main(null);

        String accountId = createAccount();

        Account account = getAccount(accountId);
        assertEquals(accountId, account.getId());
        assertEquals(0, account.getBalance());

        SubmitTransaction(new Transaction(null, accountId, 100));

        account = getAccount(accountId);
        assertEquals(accountId, account.getId());
        assertEquals(100, account.getBalance());
    }

    private void SubmitTransaction(Transaction transaction) {
        Gson gson = new GsonBuilder().create();
        String transactionJson = gson.toJson(transaction);
        Response resp = Post("http://localhost:4567/transactions/", transactionJson);
        assertEquals(200, resp.Status);
    }

    private String createAccount() {
        Response resp = Post("http://localhost:4567/accounts/", null);
        assertEquals(200, resp.Status);
        UUID.fromString(resp.body);
        return resp.body;
    }

    private Account getAccount(String accountId) {
        Response resp = Get("http://localhost:4567/accounts/" + accountId);
        assertEquals(200, resp.Status);
        Gson gson = new GsonBuilder().create();
        Account account = gson.fromJson(resp.body, Account.class);
        return account;
    }

    private Response Post(String url, String body) {
        return doRequest("POST", url, body);
    }

    private Response Get(String url) {
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
