package com.example.recipe.recipe;

import java.time.LocalDate;

/**
 * Creates day for weekly calendar
 */
@SuppressWarnings("unused")
public class Day {
    private LocalDate date;
    private int state;

    public Day(LocalDate date, int state) {
        this.date = date;
        this.state = state;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
