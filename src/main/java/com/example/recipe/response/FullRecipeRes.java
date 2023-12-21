package com.example.recipe.response;

import java.sql.Date;
import java.util.List;

public class FullRecipeRes {
    private int id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String sourceUrl;
    private String instructions;
    private String summary;
    private double healthScore;
    private int account;
    private boolean favourite;
    private boolean doLater;
    private boolean finished;
    private Date date;

    private List<String> dishTypes;
    private List<String> cuisines;
    private List<String> diets;
    private List<MeasurementRes> measurements;

    public FullRecipeRes(int id, String title, String image, int servings, int readyInMinutes, String sourceUrl, String instructions, String summary, double healthScore, int account, boolean favourite, boolean doLater, boolean finished, Date date, List<String> dishTypes, List<String> cuisines, List<String> diets, List<MeasurementRes> measurements) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
        this.instructions = instructions;
        this.summary = summary;
        this.healthScore = healthScore;
        this.account = account;
        this.favourite = favourite;
        this.doLater = doLater;
        this.finished = finished;
        this.date = date;
        this.dishTypes = dishTypes;
        this.cuisines = cuisines;
        this.diets = diets;
        this.measurements = measurements;
    }

    public FullRecipeRes() {
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

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isDoLater() {
        return doLater;
    }

    public void setDoLater(boolean doLater) {
        this.doLater = doLater;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
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
