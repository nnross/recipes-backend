package com.example.recipe.security;

import com.example.recipe.account.Account;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authorization")
public class Authorization {

    @Autowired
    RecipeRepository recipeRepository;

    public boolean isOwnRecipe(Authentication authentication, int id) {
        Account account = (Account) authentication.getPrincipal();
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) return false;
        return account.getId() == recipe.getAccount().getId();
    }

    public boolean addRecipeIsOwn(Authentication authentication, Recipe recipe) {
        Account account = (Account) authentication.getPrincipal();
        return recipe.getAccount().getId() == account.getId();
    }
}
