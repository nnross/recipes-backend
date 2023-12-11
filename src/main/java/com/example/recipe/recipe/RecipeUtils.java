package com.example.recipe.recipe;

import com.jayway.jsonpath.JsonPath;
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

    //TODO get search from frontend
    String search = "noodles";

    public RecipeResponse searchResults() {
        String request_url = "https://api.spoonacular.com/recipes";
        WebClient webClient = WebClient.create(request_url);

        return webClient.get()
                .uri("/complexSearch?apiKey="+apiKey+"&query="+search+"&number=5")
                .retrieve()
                .bodyToMono(RecipeResponse.class)
                .block();
    }
}
