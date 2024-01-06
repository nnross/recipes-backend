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
import com.example.recipe.unit.Unit;
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
     * Sets recipes date to selected and saves it to database.
     * @param recipeId
     *        id of the recipe.
     * @param date
     *        date to be set.
     * @return true if successful, error otherwise.
     */
    public Boolean setCalendar(int recipeId, String date) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));

        recipe.setToDoDate(LocalDate.parse(date));

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
            List<String> ingredients,
            List<String> cuisine,
            List<String> diet,
            List<String> intolerances,
            String type,
            String sort,
            String sortDirection,
            int page) {
        if(!ingredients.isEmpty()){
            try {
                for (String ingredient: ingredients) {
                    Ingredient.valueOf(ingredient.toUpperCase());
                }
            } catch (Exception e) {
                throw new BadRequestException("invalid ingredient filter");
            }
        }
        if(!cuisine.isEmpty()){
            try {
                for (String oneCuisine: cuisine) {
                    Cuisine.valueOf(oneCuisine.toUpperCase());
                }
            } catch (Exception e) {
                throw new BadRequestException("invalid cuisine filter");
            }
        }
        if(!diet.isEmpty()){
            try {
                for (String oneDiet: diet) {
                    Diet.valueOf(oneDiet.toUpperCase().replace(" ", "_"));
                }
            } catch (Exception e) {
                throw new BadRequestException("invalid diet filter");
            }
        }
        if(!intolerances.isEmpty()){
            try {
                for (String intolerance: intolerances) {
                    Intolerance.valueOf(intolerance.toUpperCase());
                }
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
        List<ShortRecipe> recipes = recipeUtils.searchResults(search, String.join(",", ingredients), String.join(",", cuisine), String.join(",", diet), String.join(",", intolerances), type, sort, sortDirection, offset).getResults();
        return new ListRes(recipes, !recipes.isEmpty());
    }

    /**
     * Searches the API with id.
     * @param id
     *        id of the recipe wanted
     * @return Found recipe as RecipeRes
     */
    public RecipeRes getSearchById(int id) {
        // If recipe already in database.
        if (recipeRepository.findById(id).orElse(null) != null) return null;



        RecipeFormat res = recipeUtils.getRecipeById(id);

        String summary = res.getSummary().replaceAll("<b>", "").replaceAll("</b>", "").substring(0, Math.min(res.getSummary().length(), 300))+"...";

        List<MeasurementRes> measurements = new ArrayList<>();
        for(RecipeIngredients ingredient : res.getExtendedIngredients())     {
            String unit = ingredient.getMeasures().getMetric().getUnitShort();

            MeasurementRes measurement = new MeasurementRes(
                    ingredientRepository.getIngredientByName(ingredient.getName()).orElseGet(() -> ingredientRepository.save(new com.example.recipe.ingredient.Ingredient(ingredient.getName()))),
                    ingredient.getMeasures().getMetric().getAmount(),
                    unitRepository.getUnitByName(unit).orElseGet(() -> unitRepository.save(new Unit(unit))));
            if(!measurements.contains(measurement)){
                measurements.add(measurement);
            }
        }
        String inst = res.getInstructions().replaceAll("<ol>", "").replaceAll("</ol>", "").replaceAll("<li>", "");
        List<String> instList = new ArrayList<>(Arrays.asList(inst.split("</li>")));

        List<Type> types = new ArrayList<>();
        List<Country> countries = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        for (String type : res.getDishTypes()) {
            types.add(typeRepository.getTypeByName(type).orElseGet(() -> typeRepository.save(new Type(type))));
        }

        for (String cuisine : res.getCuisines()) {
            countries.add(countryRepository.getCountryByName(cuisine).orElseGet(() -> countryRepository.save(new Country(cuisine))));
        }

        for(String category : res.getDiets()) {
            category.replaceAll("\\s", "");
            if (category.equals("pescatarian")) categories.add(categoryRepository.getCategoryByName("pescatarian").orElse(null));
            if (category.equals("nut free")) categories.add(categoryRepository.getCategoryByName("nutfree").orElse(null));
        }
        if (res.isDairyFree() && !categories.contains("dairyfree")) {
            categories.add(categoryRepository.getCategoryByName("dairyfree").orElse(null));
        }
        if (res.isVegan() && !categories.contains("vegan")) {
            categories.add(categoryRepository.getCategoryByName("vegan").orElse(null));
        }
        if (res.isVegetarian() && !categories.contains("vegetarian")) {
            categories.add(categoryRepository.getCategoryByName("vegetarian").orElse(null));
        }
        if (res.isGlutenFree() && !categories.contains("glutenfree")) {
            categories.add(categoryRepository.getCategoryByName("glutenfree").orElse(null));
        }

        return new RecipeRes(
                res.getId(),
                res.getTitle(),
                res.getImage(),
                res.getServings(),
                res.getReadyInMinutes(),
                res.getSourceUrl(),
                instList,
                summary,
                res.getHealthScore(),
                types,
                countries,
                categories,
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
        PageRequest pageRequest = PageRequest.of(page, 6);
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
        PageRequest pageRequest = PageRequest.of(page, 6);
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
        boolean isRecipe;
        boolean isFinished;
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);

            Recipe recipe = recipeRepository.getByDate(accountId, currentDate).orElse(null);
            isRecipe = recipe != null;
            isFinished = recipe != null && recipe.getFinished();

            Day day = new Day(currentDate, accountId, isRecipe, isFinished);
            weeklyCalendar.put(currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()), day);
        }
        return weeklyCalendar;
    }
}
