package com.example.recipe.response;

import java.util.List;

/**
 * Formats recipe for return as wanted
 */
public class RecipeRes {
    private int id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String sourceUrl;
    private String instructions;
    private String summary;
    private double healthScore;

    private List<String> dishTypes;
    private List<String> cuisines;
    private List<String> diets;
    private List<MeasurementRes> measurements;

    public RecipeRes(int id, String title, String image, int servings, int readyInMinutes, String sourceUrl, String instructions, String summary, double healthScore, List<String> dishTypes, List<String> cuisines, List<String> diets, List<MeasurementRes> measurements) {
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

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
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

    public List<MeasurementRes> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementRes> measurements) {
        this.measurements = measurements;
    }
}
