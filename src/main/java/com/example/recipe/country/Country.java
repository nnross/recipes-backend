package com.example.recipe.country;

import jakarta.persistence.*;

/**
 * Creates the country entity for the database.
 */
@Entity
@Table(name="country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "country_name", nullable = false, unique = true)
    private String name;

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country() {
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
