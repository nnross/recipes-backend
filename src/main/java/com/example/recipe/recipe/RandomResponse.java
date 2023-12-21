package com.example.recipe.recipe;

import com.example.recipe.apiClasses.ShortRecipe;

import java.util.List;

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
