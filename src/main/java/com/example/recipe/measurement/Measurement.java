package com.example.recipe.measurement;

import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.unit.Unit;
import jakarta.persistence.*;

/**
 * Creates the measurement entity for the database.
 */
@Entity
@Table(name="measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurement_id", nullable = false, updatable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "measurement_unit", referencedColumnName = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "measurement_ingredient", referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "measurement_amount", nullable = false)
    private float amount;

    public Measurement(int id, Unit unit, Ingredient ingredient, float amount) {
        this.id = id;
        this.unit = unit;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Measurement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
