package com.example.recipe.response;

import lombok.Data;

/**
 * Authorization response.
 * Includes the token and accountId.
 */
@Data
@SuppressWarnings("unused")
public class AuthRes {
    final private String token;
    final private int accountId;

    public AuthRes(String token, int accountId) {
        this.token = token;
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public int getAccountId() {
        return accountId;
    }
}
