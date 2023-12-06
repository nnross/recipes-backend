package com.example.recipe.response;

import lombok.Data;

@Data
public class AuthRes {
    private String token;
    private int accountId;

    public AuthRes(String token, int accountId) {
        this.token = token;
        this.accountId = accountId;
    }
}
