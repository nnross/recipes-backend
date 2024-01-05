package com.example.recipe.response;

import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.type.Type;

import java.util.List;

/**
 * Formats recipe for return as wanted
 */
@SuppressWarnings("unused")
public class RecipeRes {
    private int id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String sourceUrl;
    private List<String> instructions;
    private String summary;
    private double healthScore;

    private List<Type> dishTypes;
    private List<Country> cuisines;
    private List<Category> diets;
    private List<MeasurementRes> measurements;

    public RecipeRes(int id, String title, String image, int servings, int readyInMinutes, String sourceUrl, List<String> instructions, String summary, double healthScore, List<Type> dishTypes, List<Country> cuisines, List<Category> diets, List<MeasurementRes> measurements) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
        this.instructions = instructions;
        this.summary = summary;
        this.healthScore = healthScore;
        this.dishTypes = dishTypes;
        this.cuisines = cuisines;
        this.diets = diets;
        this.measurements = measurements;
    }

    public List<Type> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<Type> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public void setCuisines(List<Country> cuisines) {
        this.cuisines = cuisines;
    }

    public void setDiets(List<Category> diets) {
        this.diets = diets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Country> getCuisines() {
        return cuisines;
    }

    public List<Category> getDiets() {
        return diets;
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

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
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

    public List<MeasurementRes> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementRes> measurements) {
        this.measurements = measurements;
    }
}
