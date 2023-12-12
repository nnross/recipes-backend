package com.example.recipe.response;

import com.example.recipe.recipe.ListRecipeRes;

import java.util.List;

/**
 * Class to return list of recipes with data of a new page.
 */
public class ListRes {
    private List<ListRecipeRes> recipes;
    private Boolean nextPage;

    public ListRes(List<ListRecipeRes> recipes, Boolean nextPage) {
        this.recipes = recipes;
        this.nextPage = nextPage;
    }

    public List<ListRecipeRes> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ListRecipeRes> recipes) {
        this.recipes = recipes;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }
}
