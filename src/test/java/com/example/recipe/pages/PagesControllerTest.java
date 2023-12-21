package com.example.recipe.pages;

import com.example.recipe.account.Account;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.RecipeController;
import com.example.recipe.recipe.RecipeStats;
import com.example.recipe.response.*;
import com.example.recipe.security.Authorization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= RecipeController.class)
@ContextConfiguration(classes = {RecipeController.class, Authorization.class})
@EnableMethodSecurity
public class PagesControllerTest {
    @MockBean
    PagesService pagesService;

    @MockBean
    Authorization authorization;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPersonalPageWorks() throws Exception {
        ListRes recipes = new ListRes(Arrays.asList("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal?accountId=1").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0]").value(response.getRecipes()))
                .andExpect(jsonPath("$.stats.doneCount").value(response.getStats().getDone()))
                .andExpect(jsonPath("$.stats.monday.isRecipe").value(response.getCalendar().get("monday").getIsRecipe()));
    }

    @Test
    void getPersonalPageThrowsWithNoParams() throws Exception {
        ListRes recipes = new ListRes(Arrays.asList("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPersonalPageThrowsWithNotOwnAccount() throws Exception {
        ListRes recipes = new ListRes(Arrays.asList("recipe"), false);
        RecipeStats stats = new RecipeStats(null, 2, 5, 10);
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        PersonalPageRes response = new PersonalPageRes(recipes, stats, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal?accountId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
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
                "instructions",
                "summary",
                200,
                1,
                true,
                true,
                false,
                new Date(2022,12,12),
                Arrays.asList("dish"),
                Arrays.asList("cuisine"),
                Arrays.asList("diet"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar?accountId=1&date=2022-12-12").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipe.title").value(response.getRecipe().getTitle()))
                .andExpect(jsonPath("$.calendar.monday.isRecipe").value(response.getCalendar().get("monday").getIsRecipe()));
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
                "instructions",
                "summary",
                200,
                1,
                true,
                true,
                false,
                new Date(2022,12,12),
                Arrays.asList("dish"),
                Arrays.asList("cuisine"),
                Arrays.asList("diet"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar").with(csrf())
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
                "instructions",
                "summary",
                200,
                1,
                true,
                true,
                false,
                new Date(2022,12,12),
                Arrays.asList("dish"),
                Arrays.asList("cuisine"),
                Arrays.asList("diet"),
                Arrays.asList(new MeasurementRes("name", 12, "unit"))
        );
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        TodaysPageRes response = new TodaysPageRes(recipe, calendar);
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getTodays(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar?accountId=2&date=2022-12-12").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
