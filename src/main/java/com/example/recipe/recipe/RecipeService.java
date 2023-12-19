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
import com.example.recipe.measurement.MeasurementRepository;
import com.example.recipe.response.*;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.UnitRepository;
import com.example.recipe.type.Type;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    private MeasurementRepository measurementRepository;

    @Autowired
    private RecipeUtils recipeUtils;

    /**
     * Adds a recipe to the database.
     * @param recipe
     *        Recipe to be added
     * @return true if successful.
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

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new BadRequestException("error while saving to database");
        }
        return true;
    }

    /**
     * Toggles the favourite on the selected recipe
     * @param recipeId
     *        Recipe to be toggled.
     * @return true if successful
     */
    public Boolean toggleFavourite(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setFavourite(!recipe.getFavourite());

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new RuntimeException("error while saving to database");
        }
        return true;
    }

    /**
     * Toggles the doLater on the selected recipe
     * @param recipeId
     *        Recipe to be toggled.
     * @return true if successful
     */
    public Boolean toggleDoLater(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setDoLater(!recipe.getDoLater());

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new RuntimeException("error while saving to database");
        }
        return true;
    }

    /**
     * Marks the selected recipe as finished
     * @param recipeId
     *        Recipe to be marked as finished
     * @return true if successful
     */
    public Boolean finishRecipe(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        recipe.setFinished(true);

        try {
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            throw new RuntimeException("error while saving to database");
        }
        return true;
    }

    /**
     * Checks that filters and sort are correct
     * and keeps track of page
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
     * @return ListRes of recipe
     */
    public ListRes getSearch(String search, String ingredients, String cuisine, String diet, String intolerances, String type, String sort, String sortDirection, int page) {
        if(!ingredients.isEmpty()){
            try {
                Ingredient ingredient = Ingredient.valueOf(ingredients.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid ingredient filter");
            }
        }
        if(!cuisine.isEmpty()){
            try {
                Cuisine cuisines = Cuisine.valueOf(cuisine.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid cuisine filter");
            }
        }
        if(!diet.isEmpty()){
            try {
                Diet diets = Diet.valueOf(diet.toUpperCase().replaceAll(" ", "_"));
            } catch (Exception e) {
                throw new BadRequestException("invalid diet filter");
            }
        }
        if(!intolerances.isEmpty()){
            try {
                Intolerance intolerance = Intolerance.valueOf(intolerances.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid intolerance filter");
            }
        }
        if(!type.isEmpty()){
            try {
                Types types = Types.valueOf(type.toUpperCase().replaceAll(" ", "_"));
            } catch (Exception e) {
                throw new BadRequestException("invalid type filter");
            }
        }
        if(!sort.isEmpty()){
            try {
                Sort sorts = Sort.valueOf(sort.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid sort");
            }
        }
        if(!sortDirection.isEmpty()){
            try {
                SortDirection sortDirections = SortDirection.valueOf(sortDirection.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("invalid sort direction");
            }
        }
        int offset = page*12;
        List<ShortRecipe> recipes = recipeUtils.searchResults(search, ingredients, cuisine, diet, intolerances, type, sort, sortDirection, offset).getResults();
        return new ListRes(recipes, true);
    };

    /**
     * Formats ingredients correctly
     * @param id
     *       Id of the recipe wanted
     * @return Recipe as RecipeRes
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
     * Gets the statistics of recipes for specified account.
     * @param accountId
     *        id of the account we want stats for.
     * @return The stats in RecipeStats foramat
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
        PageRequest pageRequest = PageRequest.of(page, 6);
        PageRequest nextPageRequest = PageRequest.of(page + 1, 6);
        return new ListRes(recipeRepository.getFavourite(
                accountId, pageRequest),
                !recipeRepository.getFavourite(accountId, nextPageRequest).isEmpty());
    };

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
        PageRequest pageRequest = PageRequest.of(page, 6);
        PageRequest nextPageRequest = PageRequest.of(page + 1, 6);
        return new ListRes(
                recipeRepository.getDoLater(accountId, pageRequest),
                !recipeRepository.getDoLater(accountId, nextPageRequest).isEmpty());
    };


    /**
     * Gets recipe from the database.
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
            throw new RuntimeException("error while deleting from database");
        }
        return true;
    }

    /**
     * Gets recipe for date from the backend
     * @param accountId
     *        id of account of recipe
     * @param date
     *        Wanted date of recipe
     * @return recipe that matches the date.
     */
    public FullRecipeRes getRecipeForDate(int accountId, Date date) {
        Recipe recipe = recipeRepository.getByDate(accountId, date).orElse(null);
        if (recipe == null) return null;
        return converter.fullRecipeConverter(recipe);
    }

    /**
     * Gets weekly calendar for account
     * @param accountId
     *        Id of the account
     * @return Map of weekdays and Day objects
     */
    public Map<String, Day> getCalendar(int accountId) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<String, Day> weeklyCalendar = new HashMap<String, Day>();
        boolean isRecipe;
        boolean isFinished;
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            Date date = Date.valueOf(currentDate);
            try {
                Optional<Recipe> recipe = recipeRepository.getByDate(accountId, date);
                isRecipe = true;
                if (recipe.get().getFinished()) {
                    isFinished = true;
                } else {
                    isFinished = false;
                }
            } catch(Exception e) {
                isRecipe = false;
                isFinished = false;
            }
            Day day = new Day(currentDate, accountId, isRecipe, isFinished);
            weeklyCalendar.put(currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()), day);
        }
        return weeklyCalendar;
    }
}
