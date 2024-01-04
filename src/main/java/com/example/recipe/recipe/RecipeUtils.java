package com.example.recipe.recipe;

import com.example.recipe.apiClasses.RecipeFormat;
import exceptions.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

/**
 * Utils for recipes
 */
@Service
public class RecipeUtils {
    @Value("${apiKey2}")
    String apiKey;

    @Value("${apiKey}")
    String apiKey2;

    @Value("${requestUrl}")
    String request_url;

    /**
     * Gets search results from the API.
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

        try {
            return webClient.get()
                    .uri("/complexSearch?apiKey="+apiKey+"&query="+search+"&includeIngredients="+ingredients+"&cuisine="+cuisine+"&diet="+diet+"&intolerances="+intolerances+"&type="+type+"&sort="+sort+"&sortDirection="+sortDirection+"&offset="+offset+"&number=12")
                    .retrieve()
                    .bodyToMono(RecipeResponse.class)
                    .block();
        } catch (WebClientException e) {
            return webClient.get()
                    .uri("/complexSearch?apiKey="+apiKey2+"&query="+search+"&includeIngredients="+ingredients+"&cuisine="+cuisine+"&diet="+diet+"&intolerances="+intolerances+"&type="+type+"&sort="+sort+"&sortDirection="+sortDirection+"&offset="+offset+"&number=12")
                    .retrieve()
                    .bodyToMono(RecipeResponse.class)
                    .block();
        } catch (Exception e) {
            throw new ApiException("API limit reached");
        }
    }

    /**
     * Gets recipe by id from the API.
     * @param id
     *        id of the recipe wanted
     * @return Data for recipe as RecipeFormat
     */
    public RecipeFormat getRecipeById(int id) {
        WebClient webClient = WebClient.create(request_url);

        try {
            return webClient.get()
                    .uri("/"+id+"/information?apiKey="+apiKey+"&includeNutrition=false")
                    .retrieve()
                    .bodyToMono(RecipeFormat.class)
                    .block();
        } catch (WebClientException e) {
            return webClient.get()
                    .uri("/"+id+"/information?apiKey="+apiKey2+"&includeNutrition=false")
                    .retrieve()
                    .bodyToMono(RecipeFormat.class)
                    .block();
        } catch (Exception e) {
            throw new ApiException("API limit reached");
        }
    }

    /**
     * Gets 12 random recipes from the API.
     * @return 12 random recipes as RandomResponse.
     */
    public RandomResponse randomResults() {
        WebClient webClient = WebClient.create(request_url);

        try {
            return webClient.get()
                    .uri("/random?apiKey=" + apiKey + "&number=12")
                    .retrieve()
                    .bodyToMono(RandomResponse.class)
                    .block();
        } catch (WebClientException e) {
            return webClient.get()
                    .uri("/random?apiKey=" + apiKey2 + "&number=12")
                    .retrieve()
                    .bodyToMono(RandomResponse.class)
                    .block();
        } catch (Exception e) {
            throw new ApiException("API limit reached");
        }
    }
}
