package com.example.recipe.ingredient;

import jakarta.persistence.*;

/**
 * Creates the ingredient entity for the database.
 */
@Entity
@Table(name="ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    private String name;

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
