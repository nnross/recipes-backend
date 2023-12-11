package com.example.recipe.recipe;

import java.util.List;

public class RecipeResponse {
    private List<ShortRecipe> results;

    public RecipeResponse(List<ShortRecipe> results) {
        this.results = results;
    }

    public RecipeResponse() {
    }

    public List<ShortRecipe> getResults() {
        return results;
    }

    public void setResults(List<ShortRecipe> results) {
        this.results = results;
    }
}
