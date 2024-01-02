package com.example.recipe.response;

import java.util.Objects;

/**
 * Formats measurements for return as wanted
 */
@SuppressWarnings("unused")
public class MeasurementRes {
    private String name;
    private float amount;
    private String unit;

    public MeasurementRes(String name, float amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MeasurementRes other = (MeasurementRes) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(amount, other.amount)
                && Objects.equals(unit, other.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount, unit);
    }

}
