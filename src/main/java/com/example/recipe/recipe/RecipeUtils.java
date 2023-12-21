package com.example.recipe.recipe;

import com.example.recipe.apiClasses.RecipeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Utils for recipes
 */
@Service
public class RecipeUtils {
    @Value("${apiKey}")
    String apiKey;

    @Value("${requestUrl}")
    String request_url;

    /**
     * Makes the get call to the API
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
     * @param offset
     *        The amount of recipes to skip for next call
     * @return Results from call to API as RecipeResponse
     */
    public RecipeResponse searchResults(String search, String ingredients, String cuisine, String diet, String intolerances, String type, String sort, String sortDirection, int offset) {
        WebClient webClient = WebClient.create(request_url);

        return webClient.get()
                .uri("/complexSearch?apiKey="+apiKey+"&query="+search+"&includeIngredients="+ingredients+"&cuisine="+cuisine+"&diet="+diet+"&intolerances="+intolerances+"&type="+type+"&sort="+sort+"&sortDirection="+sortDirection+"&offset="+offset+"&number=12")
                .retrieve()
                .bodyToMono(RecipeResponse.class)
                .block();
    }

    /**
     * Makes the get call to the API for one recipe by id
     * @param id
     *        Id of the recipe wanted
     * @return Data for recipe by id as RecipeFormat
     */
    public RecipeFormat getRecipeById(int id) {
        WebClient webClient = WebClient.create(request_url);

        return webClient.get()
                .uri("/"+id+"/information?apiKey="+apiKey+"&includeNutrition=false")
                .retrieve()
                .bodyToMono(RecipeFormat.class)
                .block();
    }

    public RandomResponse randomResults() {
        WebClient webClient = WebClient.create(request_url);

        return webClient.get()
                .uri("/random?apiKey="+apiKey+"&number=12")
                .retrieve()
                .bodyToMono(RandomResponse.class)
                .block();
    }
}
