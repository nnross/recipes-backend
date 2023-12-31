package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.MeasurementRes;
import com.example.recipe.response.RecipeRes;
import com.example.recipe.security.Authorization;
import com.example.recipe.type.Type;
import com.example.recipe.unit.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
class RecipeControllerTest {

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
        given(authorization.addRecipeIsOwn(any(), any())).willReturn(true);

        mockMvc.perform(post("/recipe/add").with(csrf())
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
                                    "instructions": ["test instructions"],
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
                                                "ingredient": {"id": 1},
                                                "amount": 12
                                            },
                                            {
                                                "unit": {"id": 1},
                                                "ingredient": {"id": 1},
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
        given(authorization.addRecipeIsOwn(any(), any())).willReturn(true);
        given(recipeService.add(any())).willReturn(true);
        mockMvc.perform(post("/recipe/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(authorization.addRecipeIsOwn(any(), any())).willReturn(false);
        given(recipeService.add(any())).willReturn(true);
        mockMvc.perform(post("/recipe/add").with(csrf())
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
                                    "instructions": ["test instructions"],
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
                .andExpect(status().isForbidden());
    }

    @Test
    void favouriteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/favourite?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void favouriteRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/favourite", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void favouriteRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        given(recipeService.toggleFavourite(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(put("/recipe/set/favourite?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void doLaterRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/doLater?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void doLaterRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void doLaterRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(put("/recipe/set/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setDateRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.setDate(anyInt(), any())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/calendar?recipeId=1&date=2022-12-12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void setDateRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.setDate(anyInt(), any())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void setDateRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.setDate(anyInt(), any())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(put("/recipe/set/doLater", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void finishRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.finishRecipe(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/finished?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }

    @Test
    void finishRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.finishRecipe(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/recipe/set/finished", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void finishRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.toggleDoLater(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(put("/recipe/set/finished?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getRecipeSearchWorks() throws Exception {
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getSearch(any(), any(), any(), any(), any(), any(), any(), any(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/api/search?search=tes&ingredients=&cuisine=&diet=&intolerances=&type=&sort=&sortDirection=&page=0").with(csrf())
                        .with(user("test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$.recipes[1]").value(response.getRecipes().get(1)))
                .andExpect(jsonPath("$.nextPage").value(response.getNextPage()));
    }

    @Test
    void getRecipeSearchThrowsWithNoParams() throws Exception {
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getSearch(any(), any(), any(), any(), any(), any(), any(), any(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/api/search").with(csrf())
                        .with(user("test")))
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
                List.of("test instructions"),
                "test summary",
                100,
                List.of(new Type("dinner")),
                List.of(new Country("indian")),
                List.of(new Category("vegetarian")),
                List.of(new MeasurementRes(new Ingredient("ingredient"), 12, new Unit("unit")))
        );
        given(recipeService.getSearchById(anyInt())).willReturn(recipe);

        mockMvc.perform(get("/recipe/get/api/id?id=1",1).with(csrf())
                .with(user("test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.image").value(recipe.getImage()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()))
                .andExpect(jsonPath("$.readyInMinutes").value(recipe.getReadyInMinutes()))
                .andExpect(jsonPath("$.sourceUrl").value(recipe.getSourceUrl()))
                .andExpect(jsonPath("$.instructions[0]").value(recipe.getInstructions().get(0)))
                .andExpect(jsonPath("$.summary").value(recipe.getSummary()))
                .andExpect(jsonPath("$.healthScore").value(recipe.getHealthScore()))
                .andExpect(jsonPath("$.dishTypes[0].name").value(recipe.getDishTypes().get(0).getName()))
                .andExpect(jsonPath("$.cuisines.[0].name").value(recipe.getCuisines().get(0).getName()))
                .andExpect(jsonPath("$.diets[0].name").value(recipe.getDiets().get(0).getName()))
                .andExpect(jsonPath("$.measurements[0].unit.name").value(recipe.getMeasurements().get(0).getUnit().getName()))
                .andExpect(jsonPath("$.id").value(recipe.getId()));
    }

    @Test
    void getRecipeFromAPIThrowsWithNoParams() throws Exception {
        mockMvc.perform(get("/recipe/get/api/id", 1).with(csrf())
                        .with(user("test")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFavouriteRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/favourite?accountId=1&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$.recipes[1]").value(response.getRecipes().get(1)))
                .andExpect(jsonPath("$.nextPage").value(response.getNextPage()));
    }

    @Test
    void getFavouriteRecipesThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/favourite?accountId=2&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getFavouriteRecipesThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getFavourite(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/favourite", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDoLaterRecipesWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/doLater?accountId=1&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$.recipes[1]").value(response.getRecipes().get(1)))
                .andExpect(jsonPath("$.nextPage").value(response.getNextPage()));
    }

    @Test
    void getDoLaterRecipesThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/doLater?accountId=2&page=0", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getDoLaterRecipesThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getDoLater(anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/recipe/get/doLater", 1, 0).with(csrf())
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        given(recipeService.getRecipe(anyInt())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(get("/recipe/get/db?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.image").value(recipe.getImage()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()))
                .andExpect(jsonPath("$.readyInMinutes").value(recipe.getReadyInMinutes()))
                .andExpect(jsonPath("$.sourceUrl").value(recipe.getSourceUrl()))
                .andExpect(jsonPath("$.instructions[0]").value(recipe.getInstructions().get(0)))
                .andExpect(jsonPath("$.summary").value(recipe.getSummary()))
                .andExpect(jsonPath("$.healthScore").value(recipe.getHealthScore()))
                .andExpect(jsonPath("$.account").value(recipe.getAccount()))
                .andExpect(jsonPath("$.favourite").value(recipe.isFavourite()))
                .andExpect(jsonPath("$.doLater").value(recipe.isDoLater()))
                .andExpect(jsonPath("$.finished").value(recipe.isFinished()))
                .andExpect(jsonPath("$.date").value(recipe.getDate().toString()))
                .andExpect(jsonPath("$.cuisines[0]").value(recipe.getCuisines().get(0)))
                .andExpect(jsonPath("$.dishTypes[0]").value(recipe.getDishTypes().get(0)))
                .andExpect(jsonPath("$.diets[0]").value(recipe.getDiets().get(0)))
                .andExpect(jsonPath("$.measurements[0].name.name").value(recipe.getMeasurements().get(0).getName().getName()))
                .andExpect(jsonPath("$.measurements[0].amount").value(recipe.getMeasurements().get(0).getAmount()))
                .andExpect(jsonPath("$.measurements[0].unit.name").value(recipe.getMeasurements().get(0).getUnit().getName()))
                .andExpect(jsonPath("$.id").value(recipe.getId()));
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        given(recipeService.getRecipe(anyInt())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(get("/recipe/get/db?recipeId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        given(recipeService.getRecipe(anyInt())).willReturn(recipe);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(get("/recipe/get/db", 1).with(csrf())
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );

        given(recipeService.getRecipeForDate(anyInt(), any())).willReturn(recipe);

        mockMvc.perform(get("/recipe/get/date?accountId=1&date=2022-12-12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.image").value(recipe.getImage()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()))
                .andExpect(jsonPath("$.readyInMinutes").value(recipe.getReadyInMinutes()))
                .andExpect(jsonPath("$.sourceUrl").value(recipe.getSourceUrl()))
                .andExpect(jsonPath("$.instructions[0]").value(recipe.getInstructions().get(0)))
                .andExpect(jsonPath("$.summary").value(recipe.getSummary()))
                .andExpect(jsonPath("$.healthScore").value(recipe.getHealthScore()))
                .andExpect(jsonPath("$.account").value(recipe.getAccount()))
                .andExpect(jsonPath("$.favourite").value(recipe.isFavourite()))
                .andExpect(jsonPath("$.doLater").value(recipe.isDoLater()))
                .andExpect(jsonPath("$.finished").value(recipe.isFinished()))
                .andExpect(jsonPath("$.date").value(recipe.getDate().toString()))
                .andExpect(jsonPath("$.cuisines[0]").value(recipe.getCuisines().get(0)))
                .andExpect(jsonPath("$.dishTypes[0]").value(recipe.getDishTypes().get(0)))
                .andExpect(jsonPath("$.diets[0]").value(recipe.getDiets().get(0)))
                .andExpect(jsonPath("$.measurements[0].name.name").value(recipe.getMeasurements().get(0).getName().getName()))
                .andExpect(jsonPath("$.measurements[0].amount").value(recipe.getMeasurements().get(0).getAmount()))
                .andExpect(jsonPath("$.measurements[0].unit.name").value(recipe.getMeasurements().get(0).getUnit().getName()))
                .andExpect(jsonPath("$.id").value(recipe.getId()));
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        given(recipeService.getRecipeForDate(anyInt(), any())).willReturn(recipe);

        mockMvc.perform(get("/recipe/get/date?accountId=2&date=2022-12-12", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
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
                List.of("test instruction"),
                "test summary",
                120,
                account.getId(),
                false,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of("dinner"),
                List.of("indian"),
                List.of("vegan"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        given(recipeService.getRecipeForDate(anyInt(), any())).willReturn(recipe);

        mockMvc.perform(get("/recipe/get/date", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRecipeWorks() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(delete("/recipe/del?recipeId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk());
    }
    @Test
    void deleteRecipeThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(true);

        mockMvc.perform(delete("/recipe/del", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void deleteRecipeThrowsWithNotOwnRecipe() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test");

        given(recipeService.delete(anyInt())).willReturn(true);
        given(authorization.isOwnRecipe(any(), anyInt())).willReturn(false);

        mockMvc.perform(delete("/recipe/del", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRandomRecipesWork() throws Exception {
        ListRes response = new ListRes(Arrays.asList("test1", "test2"), false);

        given(recipeService.getRandom()).willReturn(response);

        mockMvc.perform(get("/recipe/get/api/random").with(csrf())
                        .with(user("test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0]").value(response.getRecipes().get(0)))
                .andExpect(jsonPath("$.recipes[1]").value(response.getRecipes().get(1)))
                .andExpect(jsonPath("$.nextPage").value(response.getNextPage()));
    }
}
