package com.example.recipe.recipe;

import com.example.recipe.apiClasses.ShortRecipe;

import java.util.List;

/**
 * Class to query random recipes from the API
 */
@SuppressWarnings("unused")
public class RandomResponse {
    private List<ShortRecipe> recipes;

    public RandomResponse(List<ShortRecipe> recipes) {
        this.recipes = recipes;
    }

    public RandomResponse() {
    }

    public List<ShortRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ShortRecipe> recipes) {
        this.recipes = recipes;
    }
}
