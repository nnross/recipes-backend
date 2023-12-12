package com.example.recipe.recipe;

import com.example.recipe.response.ListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for recipe calls
 */
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * POST API call to /api/recipe/add
     * Adds a recipe to the database
     * @param recipe
     *       New recipe as a Recipe object
     * @return true if successful.
     */
    @PreAuthorize("@authorization.addRecipeIsOwn(authentication, #recipe)")
    @PostMapping("/add")
    public Boolean add(@RequestBody Recipe recipe) {
        return recipeService.add(recipe);
    }

    /**
     * PUT API call to /api/recipe/favourite
     * Toggles the favourite on the selected recipe
     * @param recipeId
     *        id of recipe to be changed
     * @return true if successful.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/favourite")
    public Boolean favourite(@RequestParam("recipeId") int recipeId) {
        return recipeService.toggleFavourite(recipeId);
    }

    /**
     * PUT API call to /api/recipe/doLater
     * Toggles the doLater on the selected recipe
     * @param recipeId
     *        id of recipe to be changed
     * @return true if successful.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/doLater")
    public Boolean doLater(@RequestParam("recipeId") int recipeId) {
        return recipeService.toggleDoLater(recipeId);
    }

    /**
     * GET API call to /api/recipe/get/favourite
     * Gets favourite recipes for account with selected page.
     * @param accountId
     *        id of account to be searched for
     * @param page
     *        page that we want results for
     * @return ListRes object with favourite recipes and if there is a next page.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/favourite")
    public ListRes getFavourite(@RequestParam("accountId") int accountId, @RequestParam("page") int page) {
        return recipeService.getFavourite(accountId, page);
    }

    /**
     * GET API call to /api/recipe/get/doLater
     * Gets doLater recipes for account with selected page.
     * @param accountId
     *        id of account to be searched for
     * @param page
     *        page that we want results for
     * @return ListRes object with doLater recipes and if there is a next page.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/doLater")
    public ListRes getDoLater(@RequestParam("accountId") int accountId, @RequestParam("page") int page) {
        return recipeService.getDoLater(accountId, page);
    }
}
