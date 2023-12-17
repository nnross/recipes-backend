package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountService;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.RecipeRes;
import com.example.recipe.response.StatRes;
import com.example.recipe.security.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
     * with recipe in body
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
     * PUT API call to /api/recipe/favourite?recipeId=(id)
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
     * PUT API call to /api/recipe/doLater?recipeId=(id)
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
     * PUT API call to /api/recipe/finished?recipeId=(id)
     * @param recipeId
     *        id of the recipe to be marked as finished
     * @return true if successful
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/finished")
    public Boolean finished(@RequestParam("recipeId") int recipeId) {
        return recipeService.finishRecipe(recipeId);
    }

    /**
     * GET API call to /a@@pi/recipe/get/api/favourite?accountId=(id)&page=(page) TODO: ?
     * Gets the parameters from the url for search
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
     * @return RecipeService getSearch with all parameters from url
     */
    @GetMapping("/get/api/search")
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

    /**
     * GET API call to /api/recipe/get/api/id?id=(id)
     * @param id
     *       id of the recipe wanted
     * @return RecipeService getSearchById with id from url
     */
    @GetMapping("/get/api/id")
    public RecipeRes searchById(@RequestParam("id") int id) {
        return recipeService.getSearchById(id);
    }

    /**
     * GET API call to /api/recipe/get/favourite?accountId=(id)&page=(page)
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
     * GET API call to /api/recipe/get/doLater?accountId=(id)&page=(page)
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

    /**
     * GET API call to /api/recipe/get/recipe?recipeId=(id)
     * Gets recipe with specified ID.
     * @param recipeId
     *        id of recipe to be searched for.
     * @return Found recipe.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @GetMapping("/get/db")
    public FullRecipeRes getRecipeFromDB(@RequestParam("recipeId") int recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    /**
     * GET API call to /api/recipe/get/date?accountId=(id)&data=(date)
     * Gets recipe for specified date.
     * @param accountId
     *        id of account that is searching
     * @param date
     *        date of recipe we want
     * @return Found recipe.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/date")
    public FullRecipeRes getDate(@RequestParam("accountId") int accountId, @RequestParam("date") Date date) {
        return recipeService.getRecipeForDate(accountId, date);
    }

    /**
     * DELETE API call to /api/recipe/del?recipeId=(id)
     * @param recipeId
     *        Recipe of the recipe to be deleted
     * @return true if successful
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @DeleteMapping("/del")
    public Boolean delete(@RequestParam("recipeId") int recipeId) {
        return recipeService.delete(recipeId);
    }

    @GetMapping("/test")
    public RecipeStats test(@RequestParam("id") int id) {
        return recipeService.getStats(id);
    }
}
