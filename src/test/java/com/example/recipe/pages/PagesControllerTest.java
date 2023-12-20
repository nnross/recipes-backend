package com.example.recipe.pages;

import com.example.recipe.account.Account;
import com.example.recipe.recipe.RecipeController;
import com.example.recipe.response.TodaysPageRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.PersonalPageRes;
import com.example.recipe.security.Authorization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

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
        ListRes response = new PersonalPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal?accountId=1").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes[0].title").value(response.getRecipes().get(0).getTitle()))
                .andExpect(jsonPath("$.stats.doneCount").value(response.getStats().getDone()));
                //TODO: REST.
    }

    @Test
    void getPersonalPageThrowsWithNoparams() throws Exception {
        ListRes response = new PersonalPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
        //TODO: REST.
    }

    @Test
    void getPersonalPageThrowsWithNotOwnAccount() throws Exception {
        ListRes response = new PersonalPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/personal?accountId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
        //TODO: REST.
    }

    @Test
    void getCalendarPageWorks() throws Exception {
        ListRes response = new TodaysPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar?accountId=1&date=2022-12-12").with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipe.title").value(response.getRecipe().getTitle()))
                .andExpect(jsonPath("$.calendar.monday.recipeId").value(response.getCalendar().get("monday").getId()));
                //TODO: REST.
    }
    @Test
    void getCalendarPageThrowsWithNoParams() throws Exception {
        ListRes response = new TodaysPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
        //TODO: REST.
    }
    @Test
    void getCalendarPageThrowsWithNotOwnAccount() throws Exception {
        ListRes response = new TodaysPageRes();
        Account account = new Account(1, "test", "test", "test", "test");

        given(pagesService.getPersonal(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/pages/get/calendar?accountId=2&date=2022-12-12").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
