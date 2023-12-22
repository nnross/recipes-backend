package com.example.recipe.response;

import lombok.Data;

/**
 * Authorization response.
 * Includes the token and accountId.
 */
@Data
public class AuthRes {
    private String token;
    private int accountId;

    public AuthRes(String token, int accountId) {
        this.token = token;
        this.accountId = accountId;
    }
}
