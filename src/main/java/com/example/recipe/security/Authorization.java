package com.example.recipe.security;

import com.example.recipe.account.Account;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Functions to be used for authorization
 */
@Component("authorization")
public class Authorization {

    @Autowired
    RecipeRepository recipeRepository;

    /**
     * Checks if a recipe is the users own recipe.
     * @param authentication
     *        Authenticated accounts authentication.
     * @param id
     *        id of the recipe.
     * @return true if own, false otherwise.
     */
    public boolean isOwnRecipe(Authentication authentication, int id) {
        Account account = (Account) authentication.getPrincipal();
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) return false;
        return account.getId() == recipe.getAccount().getId();
    }

    /**
     * Checks if a recipe that is being added is the users own recipe.
     * @param authentication
     *        Authenticated accounts authentication.
     * @param recipe
     *        recipe to be added
     * @return true if own, false otherwise.
     */
    public boolean addRecipeIsOwn(Authentication authentication, Recipe recipe) {
        Account account = (Account) authentication.getPrincipal();
        return recipe.getAccount().getId() == account.getId();
    }
}
