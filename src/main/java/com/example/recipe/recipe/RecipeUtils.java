package com.example.recipe.recipe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utils for recipes
 */
@Service
public class RecipeUtils {
    @Value("${apiKey}")
    String apiKey;

    public RecipeResponse searchResults(String search, String ingredients, String cuisine, String diet, String intolerances, String type, String sort, String sortDirection) {
        String request_url = "https://api.spoonacular.com/recipes";
        WebClient webClient = WebClient.create(request_url);

        return webClient.get()
                .uri("/complexSearch?apiKey="+apiKey+"&query="+search+"&includeIngredients="+ingredients+"&cuisine="+cuisine+"&diet="+diet+"&intolerances="+intolerances+"&type="+type+"&sort="+sort+"&sortDirection="+sortDirection+"&number=5")
                .retrieve()
                .bodyToMono(RecipeResponse.class)
                .block();
    }
}
