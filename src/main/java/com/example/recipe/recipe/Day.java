package com.example.recipe.recipe;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Creates day for weekly calendar
 */
public class Day {
    private LocalDate date;
    private int accountId;
    private Boolean isRecipe;
    private Boolean isFinished;

    public Day(LocalDate date, int accountId, Boolean isRecipe, Boolean isFinished) {
        this.date = date;
        this.accountId = accountId;
        this.isRecipe = isRecipe;
        this.isFinished = isFinished;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsRecipe() {
        return isRecipe;
    }

    public void setIsRecipe(Boolean recipe) {
        isRecipe = recipe;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean finished) {
        isFinished = finished;
    }
}


