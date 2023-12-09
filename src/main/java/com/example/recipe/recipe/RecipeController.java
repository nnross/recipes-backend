package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountService;
import com.example.recipe.security.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PreAuthorize("#id == authentication.principal.id")
    @PostMapping("/add/favourite")
    public Boolean favourite(@RequestBody Recipe recipe, @RequestParam("accountId") int id) {
        return recipeService.favourite(recipe, id);
    }
}
