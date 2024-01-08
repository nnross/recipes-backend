package com.example.recipe.response;

import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.unit.Unit;

import java.util.Objects;

/**
 * Formats measurements for return as wanted
 */
@SuppressWarnings("unused")
public class MeasurementRes {
    private Ingredient name;
    private float amount;
    private Unit unit;

    public MeasurementRes(Ingredient name, float amount, Unit unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Ingredient getName() {
        return name;
    }

    public void setName(Ingredient name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
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
