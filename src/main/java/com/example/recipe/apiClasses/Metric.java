package com.example.recipe.apiClasses;

/**
 * Formats Metric object from API
 */
public class Metric {
    private int amount;
    private String unitShort;

    public Metric(int amount, String unitShort) {
        this.amount = amount;
        this.unitShort = unitShort;
    }

    public Metric() {

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }
}
