package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.MeasurementRes;
import com.example.recipe.response.RecipeRes;
import com.example.recipe.security.Authorization;
import com.example.recipe.type.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= RecipeController.class)
@ContextConfiguration(classes = {RecipeController.class, Authorization.class})
@EnableMethodSecurity
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @MockBean
    Authorization authorization;

    @Autowired
    MockMvc mockMvc;

    @Test
    void addRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.add(any())).willReturn(true);
        mockMvc.perform(post("/api/recipe/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "test title",
                                    "description": "test desc",
                                    "original": "test original",
                                    "time": 2,
                                    "servings": 4,
                                    "image": "test src",
                                    "favourite": false,
                                    "doLater": false,
                                    "finished": true,
                                    "toDoDate": null,
                                    "instructions": "test instructions",
                                    "healthScore": 2,
                                    "category": [{"id": 1}],
                                    "type": [{"id": 1}],
                                    "country":
                                        [
                                            {"id":1}
                                        ],
                                    "account": {"id": 1},
                                    "measurements":
                                        [
                                            {
                                                "unit": {"id": 1},
                                                "ingredient": {"id": 4},
                                                "amount": 12
                                            },
                                            {
                                                "unit": {"id": 1},
                                                "ingredient": {"id": 4},
                                                "amount": 11
                                            }
                                        ]
                                }
                                """)
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void addRecipeThrowsWithNoItem() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.add(any())).willReturn(true);
        mockMvc.perform(post("/api/recipe/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.add(any())).willReturn(true);
        mockMvc.perform(post("/api/recipe/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "test title",
                                    "description": "test desc",
                                    "original": "test original",
                                    "time": 2,
                                    "servings": 4,
                                    "image": "test src",
                                    "favourite": false,
                                    "doLater": false,
                                    "finished": true,
                                    "toDoDate": null,
                                    "instructions": "test instructions",
                                    "healthScore": 2,
                                    "category": [{"id": 1}],
                                    "type": [{"id": 1}],
                                    "country":
                                        [
                                            {"id":1}
                                        ],
                                    "account": {"id": 2},
                                    "measurements":
                                        [
                                            {
                                                "unit": {"id": 1},
                                                "ingredient": {"id": 4},
                                                "amount": 12
                                            },
                                            {
                                                "unit": {"id": 1},
                                                "ingredient": {"id": 4},
                                                "amount": 11
                                            }
                                        ]
                                }
                                """)
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void favouriteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/favourite?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void favouriteRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/favourite", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void favouriteRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(post("/api/recipe/favourite?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void doLaterRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/doLater?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void doLaterRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void doLaterRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(post("/api/recipe/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void finishRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.finishRecipe(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/finished?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void finishRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.finishRecipe(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(post("/api/recipe/finished", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void finishRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(post("/api/recipe/finished?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeSearchWorks() throws Exception {
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getSearch(any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/api/search?search=tes&ingredients=&cuisine=&diet=&intolerances=&type=&sort=&sortDirection=&page=", 1).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$res[1]").value(response.getRecipes().get(1)));
    }

    @Test
    void getRecipeSearchThrowsWithNoParams() throws Exception {
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getSearch(any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/api/search", 1).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeFromAPIWorks() throws Exception {
        RecipeRes recipe = new RecipeRes(
                1,
                "title",
                "test image",
                2,
                12,
                "test source",
                "test instructions",
                "test summary",
                100,
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegetarian"),
                Arrays.asList(new MeasurementRes("ingredient", 12, "unit"))
        );
        given(recipeService.getSearchById(anyInt())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/api/id?id=1", 1).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.image").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.servings").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.readyInMinutes").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.sourceUrl").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.instructions").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.summary").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.healthScore").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.dishTypes").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.cuisines").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.diets").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.measurements").value(recipe.getTitle()))
                .andExpect(jsonPath("$res.id").value(recipe.getId()));
    }

    @Test
    void getRecipeFromAPIThrowsWithNoParams() throws Exception {
        mockMvc.perform(get("/api/recipe/get/api/id", 1).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFavouriteRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/favourite?accountId=1&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$res[0]").value(response.getRecipes().get(1)));
    }

    @Test
    void getFavouriteRecipesThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/favourite?accountId=2&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFavouriteRecipesThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/favourite", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDoLaterRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/doLater?accountId=1&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$res[0]").value(response.getRecipes().get(1)));
    }

    @Test
    void getDoLaterRecipesThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/doLater?accountId=2&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDoLaterRecipesThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(any(), any())).willReturn(response);

        mockMvc.perform(get("/api/recipe/get/doLater", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeFromDBWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipe(any())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), any())).willReturn(true);

        mockMvc.perform(get("/api/recipe/get/db?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].image").value(recipe.getImage()))
                .andExpect(jsonPath("$res[0].servings").value(recipe.getServings()))
                .andExpect(jsonPath("$res[0].readyInMinutes").value(recipe.getReadyInMinutes()))
                .andExpect(jsonPath("$res[0].sourceUrl").value(recipe.getSourceUrl()))
                .andExpect(jsonPath("$res[0].instructions").value(recipe.getInstructions()))
                .andExpect(jsonPath("$res[0].summary").value(recipe.getSummary()))
                .andExpect(jsonPath("$res[0].healthScore").value(recipe.getHealthScore()))
                .andExpect(jsonPath("$res[0].account").value(recipe.getAccount()))
                .andExpect(jsonPath("$res[0].favourite").value(recipe.isFavourite()))
                .andExpect(jsonPath("$res[0].doLater").value(recipe.isDoLater()))
                .andExpect(jsonPath("$res[0].finished").value(recipe.isFinished()))
                .andExpect(jsonPath("$res[0].date").value(recipe.getDate()))
                .andExpect(jsonPath("$res[0].dishTypes").value(recipe.getDishTypes()))
                .andExpect(jsonPath("$res[0].cuisines").value(recipe.getCuisines()))
                .andExpect(jsonPath("$res[0].diets").value(recipe.getDiets()))
                .andExpect(jsonPath("$res[0].measurements").value(recipe.getMeasurements()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
    }

    @Test
    void getRecipeFromDBThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipe(any())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), any())).willReturn(false);

        mockMvc.perform(get("/api/recipe/get/db?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeFromDBThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipe(any())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), any())).willReturn(true);

        mockMvc.perform(get("/api/recipe/get/db", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getRecipeByDate() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipeForDate(any(), any())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/date?accountId=1&date=2022/12/12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$res[0].title").value(recipe.getTitle()))
                .andExpect(jsonPath("$res[0].image").value(recipe.getImage()))
                .andExpect(jsonPath("$res[0].servings").value(recipe.getServings()))
                .andExpect(jsonPath("$res[0].readyInMinutes").value(recipe.getReadyInMinutes()))
                .andExpect(jsonPath("$res[0].sourceUrl").value(recipe.getSourceUrl()))
                .andExpect(jsonPath("$res[0].instructions").value(recipe.getInstructions()))
                .andExpect(jsonPath("$res[0].summary").value(recipe.getSummary()))
                .andExpect(jsonPath("$res[0].healthScore").value(recipe.getHealthScore()))
                .andExpect(jsonPath("$res[0].account").value(recipe.getAccount()))
                .andExpect(jsonPath("$res[0].favourite").value(recipe.isFavourite()))
                .andExpect(jsonPath("$res[0].doLater").value(recipe.isDoLater()))
                .andExpect(jsonPath("$res[0].finished").value(recipe.isFinished()))
                .andExpect(jsonPath("$res[0].date").value(recipe.getDate()))
                .andExpect(jsonPath("$res[0].dishTypes").value(recipe.getDishTypes()))
                .andExpect(jsonPath("$res[0].cuisines").value(recipe.getCuisines()))
                .andExpect(jsonPath("$res[0].diets").value(recipe.getDiets()))
                .andExpect(jsonPath("$res[0].measurements").value(recipe.getMeasurements()))
                .andExpect(jsonPath("$res[0].id").value(recipe.getId()));
    }

    @Test
    void getRecipeByDateThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipeForDate(any(), any())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/date?accountId=2&date=2022/12/12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeByDateThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "test image",
                12,
                20,
                "test source",
                "test instruction",
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                new Date(2022, 12, 12),
                Arrays.asList("dinner"),
                Arrays.asList("indian"),
                Arrays.asList("vegan"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        given(recipeService.getRecipeForDate(any(), any())).willReturn(recipe);

        mockMvc.perform(get("/api/recipe/get/date", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), any())).willReturn(true);

        mockMvc.perform(delete("/api/recipe/del?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }
    @Test
    void deleteRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), any())).willReturn(true);

        mockMvc.perform(delete("/api/recipe/del", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void deleteRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), any())).willReturn(false);

        mockMvc.perform(delete("/api/recipe/del", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
}
