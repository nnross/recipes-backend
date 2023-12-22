package com.example.recipe.apiClasses;

/**
 * Formats Ingredient object from API
 */
@SuppressWarnings("unused")
public class RecipeIngredients {
    private String name;

    private Measures measures;

    public RecipeIngredients(String name, Measures measures) {
        this.name = name;
        this.measures = measures;
    }

    public RecipeIngredients() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Measures getMeasures() {
        return measures;
    }

    public void setMeasures(Measures measures) {
        this.measures = measures;
    }
}
