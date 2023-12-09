package com.example.recipe.unit;

import jakarta.persistence.*;

/**
 * Creates the unit entity for the database.
 */
@Entity
@Table(name="unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "unit_name", nullable = false, unique = true)
    private String name;

    public Unit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Unit() {
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
