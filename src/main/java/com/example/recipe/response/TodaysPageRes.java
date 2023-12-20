package com.example.recipe.response;

import com.example.recipe.recipe.Day;

import java.util.Map;

/**
 * Class to return todays page data.
 */
public class TodaysPageRes {
    FullRecipeRes recipe;
    Map<String, Day> calendar;

    public TodaysPageRes(FullRecipeRes recipe, Map<String, Day> calendar) {
        this.recipe = recipe;
        this.calendar = calendar;
    }

    public FullRecipeRes getRecipe() {
        return recipe;
    }

    public void setRecipe(FullRecipeRes recipe) {
        this.recipe = recipe;
    }

    public Map<String, Day> getCalendar() {
        return calendar;
    }

    public void setCalendar(Map<String, Day> calendar) {
        this.calendar = calendar;
    }
}
