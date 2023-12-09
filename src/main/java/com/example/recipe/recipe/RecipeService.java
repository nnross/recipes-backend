package com.example.recipe.recipe;

import org.springframework.stereotype.Service;

@Service
public class RecipeService {


    public Boolean add(Recipe recipe) {
        return true;
    }

    public Boolean favourite(int recipeId) {
        return true;
    }

    public Boolean doLater(int recipeId) {
        return true;
    }
}
