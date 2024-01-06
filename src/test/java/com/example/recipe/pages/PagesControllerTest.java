package com.example.recipe.pages;

import com.example.recipe.account.Account;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.RecipeStats;
import com.example.recipe.response.*;
import com.example.recipe.unit.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= PagesController.class)
@ContextConfiguration(classes = PagesController.class)
@EnableMethodSecurity
class PagesControllerTest {
    @MockBean
    PagesService pagesService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPersonalPageWorks() throws Exception {
        ListRes recipes = new ListRes(List.of("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/personal?accountId=1").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes.recipes[0]").value(response.getRecipes().getRecipes().get(0)))
                .andExpect(jsonPath("$.stats.done").value(response.getStats().getDone()))
                .andExpect(jsonPath("$.calendar.monday.state").value(0))
                .andExpect(jsonPath("$.calendar.monday.date").value("2022-12-12"));
    }

    @Test
    void getPersonalPageThrowsWithNoParams() throws Exception {
        ListRes recipes = new ListRes(List.of("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/personal").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPersonalPageThrowsWithNotOwnAccount() throws Exception {
        ListRes recipes = new ListRes(List.of("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/personal?accountId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTodaysPageWorks() throws Exception {
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "image",
                4,
                10,
                "source",
                List.of("instructions"),
                "summary",
                200,
                1,
                true,
                true,
                false,
                LocalDate.of(2022,12,12),
                List.of("dish"),
                List.of("cuisine"),
                List.of("diet"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/todays?accountId=1").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipe.title").value(response.getRecipe().getTitle()))
                .andExpect(jsonPath("$.calendar.monday.state").value(0))
                .andExpect(jsonPath("$.calendar.monday.date").value("2022-12-12"));
    }
    @Test
    void getCalendarPageThrowsWithNoParams() throws Exception {
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "image",
                4,
                10,
                "source",
                List.of("instructions"),
                "summary",
                200,
                1,
                true,
                true,
                false,
                LocalDate.of(2022,12,12),
                List.of("dish"),
                List.of("cuisine"),
                List.of("diet"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/todays").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getCalendarPageThrowsWithNotOwnAccount() throws Exception {
        FullRecipeRes recipe = new FullRecipeRes(
                1,
                "title",
                "image",
                4,
                10,
                "source",
                List.of("instructions"),
                "summary",
                200,
                1,
                true,
                true,
                false,
                LocalDate.of(2022,12,12),
                List.of("dish"),
                List.of("cuisine"),
                List.of("diet"),
                List.of(new MeasurementRes(new Ingredient("name"), 12, new Unit("unit")))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/pages/get/todays?accountId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
