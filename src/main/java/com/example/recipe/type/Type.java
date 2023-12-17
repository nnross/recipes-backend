package com.example.recipe.type;

import jakarta.persistence.*;

/**
 * Creates the type entity for the database.
 */
@Entity
@Table(name="type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "type_name", unique = true)
    private String name;

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type() {
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
