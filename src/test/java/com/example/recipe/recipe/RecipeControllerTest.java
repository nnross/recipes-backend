package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountService;
import com.example.recipe.recipe.RecipeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= RecipeController.class)
@ContextConfiguration(classes = RecipeController.class)
@EnableMethodSecurity
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getFavouriteRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListResRecipe recipe = new ListResRecipe(
                1,
                "title"
        );
        List<ListResRecipe> res = new ArrayList<ListResRecipe>();
        res.add(recipe);
        given(recipeService.getFavourite(anyInt())).willReturn(res);

        mockMvc.perform(get("/api/recipe/get/favourite?accountId=1&page=0", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
    }
    @Test
    void getDoLaterRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListResRecipe recipe = new ListResRecipe(
                1,
                "title"
        );
        List<ListResRecipe> res = new ArrayList<ListResRecipe>();
        res.add(ResRecipe);
        given(recipeService.getDoLater(anyInt())).willReturn(res);

        mockMvc.perform(get("/api/recipe/get/doLater?accountId=1&page=0", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
    }
    @Test
    void getRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ResRecipe recipe = new ResRecipe(
                1,
                "title",
                "test category",
                "test type",
                "test user",
                "test desc",
                1300,
                5,
                "test img",
                true,
                true,
                false,
                "test country",
                new Date(),
                "test measurements",
                "test instructions"
        );
        given(recipeService.getRecipeFromDB(anyInt())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/db?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
        //TODO: rest
    }
    @Test
    void getRecipeFromAPIWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ResRecipe recipe = new ResRecipe(
                1,
                "title",
                "test category",
                "test type",
                "test user",
                "test desc",
                1300,
                5,
                "test img",
                true,
                true,
                false,
                "test country",
                new Date(),
                "test measurements",
                "test instructions"
        );
        given(recipeService.getRecipeFromAPI(anyInt())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/api?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
        //TODO: rest
    }
    @Test
    void getRecipeByDate() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ResRecipe recipe = new ResRecipe(
                1,
                "title",
                "test category",
                "test type",
                "test user",
                "test desc",
                1300,
                5,
                "test img",
                true,
                true,
                false,
                "test country",
                new Date(),
                "test measurements",
                "test instructions"
        );
        given(recipeService.getRecipeByDate(anyInt())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/date?date=2022/12/12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
        //TODO: rest
    }
    @Test
    void getSearchWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ResRecipe recipe = new ResRecipe(
                1,
                "title",
                "test category",
                "test type",
                "test user",
                "test desc",
                1300,
                5,
                "test img",
                true,
                true,
                false,
                "test country",
                new Date(),
                "test measurements",
                "test instructions"
        );
        given(recipeService.getRecipeBySearch(anyInt())).willReturn(recipe);

        // TODO: correct url query
        mockMvc.perform(get("/api/recipe/get/search?sort=dateAsc&search=tes&filters={country:[italy]}", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
        //TODO: rest
    }

    @Test
    void deleteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);

        mockMvc.perform(delete("/api/recipe/delete?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }
    @Test
    void favouriteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.favourite(anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/favourite?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }
    @Test
    void addRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.add(any())).willReturn(true);

        mockMvc.perform(post("/api/recipe/add").with(csrf())
                        .with(user(account)))
                //TODO: REST
                .andExpect(status().isOk());
    }
    @Test
    void doLaterRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.doLater(anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/doLater?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }
}
