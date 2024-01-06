package com.example.recipe.recipe;

import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.RecipeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for recipe calls
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * POST API call to /recipe/add
     * with recipe in body
     * Adds a recipe to the database
     * Authorization with addRecipeIsOwn().
     * @param recipe
     *        New recipe as a Recipe object
     * @return true if successful, error otherwise.
     */
    @PreAuthorize("@authorization.addRecipeIsOwn(authentication, #recipe)")
    @PostMapping("/add")
    public Boolean add(@RequestBody Recipe recipe) {
        return recipeService.add(recipe);
    }

    /**
     * PUT API call to /recipe/favourite?recipeId=(id)
     * Toggles favourite on the selected recipe
     * Authorization with isOwnRecipe()
     * @param recipeId
     *        id of recipe to be changed
     * @return true if successful, error otherwise.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/set/favourite")
    public Boolean favourite(@RequestParam("recipeId") int recipeId) {
        return recipeService.toggleFavourite(recipeId);
    }

    /**
     * PUT API call to /recipe/doLater?recipeId=(id)
     * Toggles doLater on the selected recipe
     * Authorization with isOwnRecipe()
     * @param recipeId
     *        id of recipe to be changed
     * @return true if successful, error otherwise.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/set/doLater")
    public Boolean doLater(@RequestParam("recipeId") int recipeId) {
        return recipeService.toggleDoLater(recipeId);
    }


    /**
     * PUT API call to /recipe/calendar?recipeId=(id)&date=(date)
     * sets a new date for selected recipe
     * Authorization with isOwnRecipe()
     * @param recipeId
     *        id of the recipe to set date.
     * @param date
     *        date to be set.
     * @return true id successful, error otherwise.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/set/calendar")
    public Boolean calendar(@RequestParam("recipeId") int recipeId, @RequestParam("date") String date) {
        return recipeService.setCalendar(recipeId, date);
    }


    /**
     * PUT API call to /recipe/finished?recipeId=(id)
     * Sets recipe as finished.
     * Authorization with isOwnRecipe()
     * @param recipeId
     *        id of the recipe to be marked as finished
     * @return true if successful, error otherwise.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @PutMapping("/set/finished")
    public Boolean finished(@RequestParam("recipeId") int recipeId) {
        return recipeService.finishRecipe(recipeId);
    }

    /**
     * GET API call to /recipe/get/api/search
     *         ?search=(search)?ingredients=(ingredients)?cuisine=(cuisine)?diet=(diet)?intolerances=(intolerances)
     *         ?type=(type)?sort=(sort)?sortDirection=(sortDirection)?page=(page)
     * If multiple filters then divide with comma for example diets=vegan,vegetarian.
     * Searches with the API for the recipe and returns list of results.
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
     * @return List of results from API and if there is more results.
     */
    @GetMapping("/get/api/search")
    public ListRes search(@RequestParam("search") String search,
                          @RequestParam("ingredients") List<String> ingredients,
                          @RequestParam("cuisine") List<String> cuisine,
                          @RequestParam("diet") List<String> diet,
                          @RequestParam("intolerances") List<String> intolerances,
                          @RequestParam("type") String type,
                          @RequestParam("sort") String sort,
                          @RequestParam("sortDirection") String sortDirection,
                          @RequestParam("page") int page){
        return recipeService.getSearch(search, ingredients, cuisine, diet, intolerances, type, sort, sortDirection, page);
    }

    /**
     * GET API call to /recipe/get/api/id=(id)
     * Gets the recipe with id from the API.
     * @param id
     *       id of the recipe wanted
     * @return Recipe from the API.
     */
    @GetMapping("/get/api/id")
    public RecipeRes searchById(@RequestParam("id") int id) {
        return recipeService.getSearchById(id);
    }

    /**
     * GET API call to /recipe/get/api/random
     * Gets 12 random recipes from the API.
     * @return 12 random recipes
     */
    @GetMapping("/get/api/random")
    public ListRes getRandom() { return recipeService.getRandom(); }

    /**
     * GET API call to /recipe/get/favourite?accountId=(id)&page=(page)
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
     * GET API call to /recipe/get/doLater?accountId=(id)&page=(page)
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
     * GET API call to /recipe/get/recipe?recipeId=(id)
     * Gets recipe with specified ID from the database.
     * @param recipeId
     *        id of recipe to be searched for.
     * @return Recipe from the database.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @GetMapping("/get/db")
    public FullRecipeRes getRecipeFromDB(@RequestParam("recipeId") int recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    /**
     * GET API call to /recipe/get/date?accountId=(id)&data=(date)
     * Gets recipe for specified date.
     * @param accountId
     *        id of account that is searching
     * @param date
     *        date of recipe we want
     * @return Found recipe. If no recipe for date then just null.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/date")
    public FullRecipeRes getDate(@RequestParam("accountId") int accountId, @RequestParam("date") LocalDate date) {
        return recipeService.getRecipeForDate(accountId, date);
    }

    /**
     * DELETE API call to /api/recipe/del?recipeId=(id)
     * Deletes recipe with ID from the database.
     * @param recipeId
     *        id of the recipe to be deleted
     * @return true if successful, error otherwise.
     */
    @PreAuthorize("@authorization.isOwnRecipe(authentication, #recipeId)")
    @DeleteMapping("/del")
    public Boolean delete(@RequestParam("recipeId") int recipeId) {
        return recipeService.delete(recipeId);
    }
}
