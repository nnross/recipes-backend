package com.example.recipe.recipe;

import com.example.recipe.apiClasses.RecipeFormat;
import com.example.recipe.apiClasses.RecipeIngredients;
import com.example.recipe.apiClasses.ShortRecipe;
import com.example.recipe.category.Category;
import com.example.recipe.category.CategoryRepository;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.enums.*;
import com.example.recipe.ingredient.IngredientRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.response.*;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.UnitRepository;
import com.example.recipe.type.Type;
import exceptions.BadRequestException;
import exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Logic for recipe calls.
 */
@Service
public class RecipeService {
    Converters converter = new Converters();

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private RecipeUtils recipeUtils;

    /**
     * Adds a recipe to the database.
     * @param recipe
     *        Recipe to be added
     * @return true if successful, error otherwise.
     */
    public Boolean add(Recipe recipe) {
        for (Measurement measurement : recipe.getMeasurements()) {
            unitRepository.findById(measurement.getUnit().getId()).orElseThrow(() ->
                new BadRequestException("unit not in database"));
            ingredientRepository.findById(measurement.getIngredient().getId()).orElseThrow(() ->
                    new BadRequestException("ingredient not in database"));
            }

        for (Type type : recipe.getType()) {
            typeRepository.findById(type.getId()).orElseThrow(() ->
                    new BadRequestException("type not in database"));
        }

        for (Country country : recipe.getCountry()) {
            countryRepository.findById(country.getId()).orElseThrow(() ->
                    new BadRequestException("country not in database"));
        }

        for (Category category : recipe.getCategory()) {
            categoryRepository.findById(category.getId()).orElseThrow(() ->
                    new BadRequestException("category not in database"));
        }

        if (recipeRepository.getByDate(recipe.getAccount().getId(), recipe.getToDoDate()).orElse(null) != null) {
            throw new BadRequestException("recipe with date already exists");
        }

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new DatabaseException("error while saving to database");
        }
        return true;
    }

    /**
     * Toggles the favourite on the selected recipe
     * @param recipeId
     *        Recipe to be toggled.
     * @return true if successful, error otherwise.
     */
    public Boolean toggleFavourite(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setFavourite(!recipe.getFavourite());

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new DatabaseException("error while saving to database");
        }
        return true;
    }

    /**
     * Toggles the doLater on the selected recipe
     * @param recipeId
     *        Recipe to be toggled.
     * @return true if successful, error otherwise
     */
    public Boolean toggleDoLater(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setDoLater(!recipe.getDoLater());

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new DatabaseException("error while saving to database");
        }
        return true;
    }

    /**
     * Sets date for recipe
     * @param recipeId
     *        Recipe to be toggled.
     * @param date
     *        date to be set.
     * @return true if successful, error otherwise
     */
    public Boolean setDate(int recipeId, LocalDate date) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setToDoDate(date);

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new DatabaseException("error while saving to database");
        }
        return true;
    }

    /**
     * Marks the selected recipe as finished
     * @param recipeId
     *        Recipe to be marked as finished
     * @return true if successful, error otherwise.
     */
    public Boolean finishRecipe(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setFinished(true);

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new DatabaseException("error while saving to database");
        }
        return true;
    }

    /**
     * Searches the API for results with selected filters.
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
     * @return ListRes of recipes
     */
    public ListRes getSearch(
            String search,
            String ingredients,
            String cuisine,
            String diet,
            String intolerances,
            String type,
            String sort,
            String sortDirection,
            int page) {
        if(!ingredients.isEmpty()){
            try {
                Ingredient.valueOf(ingredients.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid ingredient filter");
            }
        }
        if(!cuisine.isEmpty()){
            try {
                Cuisine.valueOf(cuisine.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid cuisine filter");
            }
        }
        if(!diet.isEmpty()){
            try {
                Diet.valueOf(diet.toUpperCase().replace(" ", "_"));
            } catch (Exception e) {
                throw new BadRequestException("invalid diet filter");
            }
        }
        if(!intolerances.isEmpty()){
            try {
                Intolerance.valueOf(intolerances.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid intolerance filter");
            }
        }
        if(!type.isEmpty()){
            try {
                Types.valueOf(type.toUpperCase().replace(" ", "_"));
            } catch (Exception e) {
                throw new BadRequestException("invalid type filter");
            }
        }
        if(!sort.isEmpty()){
            try {
                Sort.valueOf(sort.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid sort");
            }
        }
        if(!sortDirection.isEmpty()){
            try {
                SortDirection.valueOf(sortDirection.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid sort direction");
            }
        }
        int offset = page*12;
        List<ShortRecipe> recipes = recipeUtils.searchResults(search, ingredients, cuisine, diet, intolerances, type, sort, sortDirection, offset).getResults();
        return new ListRes(recipes, true);
    }

    /**
     * Searches the API with id.
     * @param id
     *        id of the recipe wanted
     * @return Found recipe as RecipeRes
     */
    public RecipeRes getSearchById(int id) {
        RecipeFormat res = recipeUtils.getRecipeById(id);

        List<MeasurementRes> measurements = new ArrayList<>();
        for(RecipeIngredients ingredient : res.getExtendedIngredients())     {
            measurements.add(new MeasurementRes(
                    ingredient.getName(),
                    ingredient.getMeasures().getMetric().getAmount(),
                    ingredient.getMeasures().getMetric().getUnitShort()));
        }
        List<String> diets = new ArrayList<>();
        if (res.isDairyFree()) {
            diets.add("dairy free");
        }
        if (res.isVegan()) {
            diets.add("vegan");
        }
        if (res.isVegetarian()) {
            diets.add("vegetarian");
        }
        if (res.isGlutenFree()) {
            diets.add("gluten free");
        }

        diets.addAll(res.getDiets());

        return new RecipeRes(
                res.getId(),
                res.getTitle(),
                res.getImage(),
                res.getServings(),
                res.getReadyInMinutes(),
                res.getSourceUrl(),
                res.getInstructions(),
                res.getSummary(),
                res.getHealthScore(),
                res.getDishTypes(),
                res.getCuisines(),
                diets,
                measurements
        );
    }

    /**
     * Gets random recipes from the API.
     * @return a ListRes of random recipes
     */
    public ListRes getRandom() {
        List<ShortRecipe> recipes = recipeUtils.randomResults().getRecipes();
        return new ListRes(recipes, false);
    }

    /**
     * Gets the statistics of recipes for specified account.
     * @param accountId
     *        id of the account we want stats for.
     * @return The stats in RecipeStats format
     */
    public RecipeStats getStats(int accountId) {
        RecipeStats res = new RecipeStats();
        res.setChart(recipeRepository.getStats(accountId));

        res.setDone(recipeRepository.getDoneCount(accountId).orElseThrow(() ->
                new BadRequestException("error while getting done count from database")));

        res.setFavourite(recipeRepository.getFavouriteCount(accountId).orElseThrow(() ->
                new BadRequestException("error while getting favourite count from database")));

        res.setDoLater(recipeRepository.getDoLaterCount(accountId).orElseThrow(() ->
                new BadRequestException("error while getting doLater count from database")));

        return res;
    }

    /**
     * Gets favourite recipes with page from database.
     * Also checks if there is a next page and converts into a ListRes.
     * @param accountId
     *        id of account we want favourites for.
     * @param page
     *        Page of recipes we want.
     * @return ListRes with favourite recipes and if there is a nextPage
     */
    public ListRes getFavourite(int accountId, int page) {
        if (page < 0) {
            throw new BadRequestException("Invalid page");
        }
        PageRequest pageRequest = PageRequest.of(page, 5);
        PageRequest nextPageRequest = PageRequest.of(page + 1, 6);
        return new ListRes(recipeRepository.getFavourite(
                accountId, pageRequest),
                !recipeRepository.getFavourite(accountId, nextPageRequest).isEmpty());
    }

    /**
     * Gets doLater recipes with page from database.
     * Also checks if there is a next page and converts into a ListRes.
     * @param accountId
     *        id of account we want doLater for.
     * @param page
     *        Page of recipes we want.
     * @return ListRes with doLater recipes and if there is a nextPage
     */
    public ListRes getDoLater(int accountId, int page) {
        if (page < 0) {
            throw new BadRequestException("Invalid page");
        }
        PageRequest pageRequest = PageRequest.of(page, 5);
        PageRequest nextPageRequest = PageRequest.of(page + 1, 6);
        return new ListRes(
                recipeRepository.getDoLater(accountId, pageRequest),
                !recipeRepository.getDoLater(accountId, nextPageRequest).isEmpty());
    }


    /**
     * Gets recipe from the database with id.
     * @param recipeId
     *        id of the recipe we want to search.
     * @return found recipe.
     */
    public FullRecipeRes getRecipe(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        return converter.fullRecipeConverter(recipe);
    }

    /**
     * Deletes the wanted recipe from the database
     * @param id
     *       The recipe to be deleted
     * @return true if successful
     */
    public Boolean delete(int id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.getCountry().clear();
        recipe.getType().clear();
        recipe.getCategory().clear();
        try {
            recipeRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new DatabaseException("error while deleting from database");
        }
        return true;
    }

    /**
     * Gets recipe for date from the backend
     * @param accountId
     *        id of account of recipe
     * @param date
     *        Wanted date of recipe
     * @return recipe that matches the date. If no recipe return null.
     */
    public FullRecipeRes getRecipeForDate(int accountId, LocalDate date) {
        Recipe recipe = recipeRepository.getByDate(accountId, date).orElse(null);
        if (recipe == null) return null;
        return converter.fullRecipeConverter(recipe);
    }

    /**
     * Gets weekly calendar for account
     * @param accountId
     *        id of the account
     * @return Map of weekdays and Day objects
     */
    public Map<String, Day> getCalendar(int accountId) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<String, Day> weeklyCalendar = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);

            Recipe recipe = recipeRepository.getByDate(accountId, currentDate).orElse(null);
            int state = 0;
            if (recipe != null) state = 1;
            if (recipe != null && recipe.getFinished()) state = 2;


            weeklyCalendar.put(
                    currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    new Day(currentDate, state));
        }
        return weeklyCalendar;
    }
}
