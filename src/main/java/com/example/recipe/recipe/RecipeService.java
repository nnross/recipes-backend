package com.example.recipe.recipe;

import com.example.recipe.category.CategoryRepository;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.ingredient.IngredientRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.measurement.MeasurementRepository;
import com.example.recipe.response.ListRes;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.UnitRepository;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Logic for recipe calls.
 */
@Service
public class RecipeService {

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

    /**
     * Adds a recipe to the database.
     * @param recipe
     *        Recipe to be added
     * @return true if successful.
     */
    public Boolean add(Recipe recipe) {
        for (Measurement measurement : recipe.getMeasurements()) {
            unitRepository.findById(measurement.getUnit().getId()).orElseThrow(() ->
                new BadRequestException("measurement not in database"));
            ingredientRepository.findById(measurement.getIngredient().getId()).orElseThrow(() ->
                    new BadRequestException("ingredient not in database"));
            }
        typeRepository.findById(recipe.getType().getId()).orElseThrow(() ->
                new BadRequestException("type not in database"));
        countryRepository.findById(recipe.getCountry().getId()).orElseThrow(() ->
                new BadRequestException("country not in database"));
        categoryRepository.findById(recipe.getCategory().getId()).orElseThrow(() ->
                new BadRequestException("category not in database"));
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
    public Object getRecipe(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new BadRequestException("no recipe with id"));
        // TODO: convert into returnable recipe
        return recipe;
    }
}
