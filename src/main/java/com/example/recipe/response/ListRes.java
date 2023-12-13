package com.example.recipe.response;

import org.hibernate.mapping.Any;

import java.util.List;

/**
 * Class to return list of recipes with data of a new page.
 */
public class ListRes {
    private List<?> recipes;
    private Boolean nextPage;

    public ListRes(List<?> recipes, Boolean nextPage) {
        this.recipes = recipes;
        this.nextPage = nextPage;
    }

    public List<?> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<?> recipes) {
        this.recipes = recipes;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }
}