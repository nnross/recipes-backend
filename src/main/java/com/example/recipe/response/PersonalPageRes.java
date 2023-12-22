package com.example.recipe.response;

import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.RecipeStats;

import java.util.Map;

/**
 * Class to return personal page data.
 */
@SuppressWarnings("unused")
public class PersonalPageRes {
    private ListRes recipes;
    private RecipeStats stats;
    private Map<String, Day> calendar;

    public PersonalPageRes(ListRes recipes, RecipeStats stats, Map<String, Day> calendar) {
        this.recipes = recipes;
        this.stats = stats;
        this.calendar = calendar;
    }

    public ListRes getRecipes() {
        return recipes;
    }

    public void setRecipes(ListRes recipes) {
        this.recipes = recipes;
    }

    public RecipeStats getStats() {
        return stats;
    }

    public void setStats(RecipeStats stats) {
        this.stats = stats;
    }

    public Map<String, Day> getCalendar() {
        return calendar;
    }

    public void setCalendar(Map<String, Day> calendar) {
        this.calendar = calendar;
    }
}
