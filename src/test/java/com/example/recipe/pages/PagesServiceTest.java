package com.example.recipe.pages;

import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.RecipeRepository;
import com.example.recipe.recipe.RecipeService;
import com.example.recipe.recipe.RecipeStats;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.security.JwtService;
import exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = {RecipeApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class PagesServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeService recipeService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private PagesService testPagesService;

    @Test
    void getPersonalPageWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        given(recipeService.getCalendar(anyInt())).willReturn(calendar);
        given(recipeService.getStats(anyInt())).willReturn(new RecipeStats());

        given(recipeService.getFavourite(anyInt(), anyInt())).willReturn(new ListRes(List.of("test"), false));

        testPagesService.getPersonal(1);

        verify(recipeService).getCalendar(1);
        verify(recipeService).getStats(1);
        verify(recipeService).getFavourite(1, 0);
    }

    @Test
    void getPersonalPageThrowsWithNoAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> testPagesService.getPersonal(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no account with id");
    }

    @Test
    void getCalendarPageWorks() {
        given(accountRepository.findById(anyInt())).willReturn(Optional.of(new Account()));
        given(recipeService.getRecipeForDate(anyInt(), any())).willReturn(new FullRecipeRes());
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 0));

        given(recipeService.getCalendar(anyInt())).willReturn(calendar);

        testPagesService.getTodays(1, "2022-12-12");

        verify(recipeService).getRecipeForDate(1, LocalDate.parse("2022-12-12"));
        verify(recipeService).getCalendar(1);
    }

    @Test
    void getCalendarPageThrowsWithNoAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getTodays(1, "2022-12-12"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no account with id");
    }
}
