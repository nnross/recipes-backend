package com.example.recipe.apiClasses;

/**
 * Formats Measures object from API
 */
public class Measures {
    private Metric metric;

    public Measures(Metric metric) {
        this.metric = metric;
    }

    public Measures() {

    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
}
