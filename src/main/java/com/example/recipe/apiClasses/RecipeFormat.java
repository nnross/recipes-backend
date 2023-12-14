package com.example.recipe.apiClasses;

import java.util.List;

/**
 * Formats recipe from API
 */
public class RecipeFormat {
    private int id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String sourceUrl;
    private String instructions;
    private String summary;
    private double healthScore;
    private boolean dairyFree;
    private boolean glutenFree;
    private boolean vegan;
    private boolean vegetarian;
    private List<String> cuisines;
    private List<String> diets;
    private List<RecipeIngredients> extendedIngredients;

    public RecipeFormat(int id, String title, String image, int servings, int readyInMinutes, String sourceUrl, String instructions, String summary, double healthScore, boolean dairyFree, boolean glutenFree, boolean vegan, boolean vegetarian, List<String> cuisines, List<String> diets, List<RecipeIngredients> extendedIngredients) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
        this.instructions = instructions;
        this.summary = summary;
        this.healthScore = healthScore;
        this.dairyFree = dairyFree;
        this.glutenFree = glutenFree;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.cuisines = cuisines;
        this.diets = diets;
        this.extendedIngredients = extendedIngredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public List<RecipeIngredients> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<RecipeIngredients> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
}
