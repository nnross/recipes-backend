package com.example.recipe.recipe;

import com.example.recipe.response.ResStat;

import java.util.List;

/**
 * Class to return recipe statistics.
 */
public class RecipeStats {

    private List<ResStat> chart;
    private int done;
    private int favourite;
    private int doLater;

    public RecipeStats(List<ResStat> chart, int done, int favourite, int doLater) {
        this.chart = chart;
        this.done = done;
        this.favourite = favourite;
        this.doLater = doLater;
    }

    public RecipeStats() {
    }

    public List<ResStat> getChart() {
        return chart;
    }

    public void setChart(List<ResStat> chart) {
        this.chart = chart;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getDoLater() {
        return doLater;
    }

    public void setDoLater(int doLater) {
        this.doLater = doLater;
    }
}
