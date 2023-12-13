package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountService;
import com.example.recipe.response.ListRes;
import com.example.recipe.security.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Gets the parameters from the url
     * @param search
     *        Recipes to get from the API
     * @param ingredients
     *        Ingredients included in the recipe
     * @param cuisine
     *        From which origin the recipe can be
     * @param diet
     *        Dietary restrictions of the recipe
     * @param intolerances
     *        Food intolerances
     * @param type
     *        The type of food the recipe is, e.g. breakfast
     * @param sort
     *        Ways to sort the results
     * @param sortDirection
     *        Ascending or descending
     * @param page
     *        Keeps track of the page showing the results
     * @return RecipeService with all parameters from url
     */
    @GetMapping("/get/search")
    public ListRes search(@RequestParam("search") String search,
                          @RequestParam("ingredients") String ingredients,
                          @RequestParam("cuisine") String cuisine,
                          @RequestParam("diet") String diet,
                          @RequestParam("intolerances") String intolerances,
                          @RequestParam("type") String type,
                          @RequestParam("sort") String sort,
                          @RequestParam("sortDirection") String sortDirection,
                          @RequestParam("page") int page){
        return recipeService.getSearch(search, ingredients, cuisine, diet, intolerances, type, sort, sortDirection, page);
    }
}
