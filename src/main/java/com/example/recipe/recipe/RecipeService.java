package com.example.recipe.recipe;

import com.example.recipe.account.AccountRepository;
import com.example.recipe.category.CategoryRepository;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.ingredient.IngredientRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.measurement.MeasurementRepository;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.UnitRepository;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Object getSearch(String search, String ingredients, String cuisine, String diet, String intolerances, String type, String sort, String sortDirection) {
        return recipeUtils.searchResults(search, ingredients, cuisine, diet, intolerances, type, sort, sortDirection).getResults();
    };
}
