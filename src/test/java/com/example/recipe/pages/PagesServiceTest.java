package com.example.recipe.pages;

import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.recipe.*;
import com.example.recipe.response.StatRes;
import com.example.recipe.security.JwtService;
import com.example.recipe.unit.Unit;
import exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = {RecipeApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class PagesServiceTest {

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
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        given(recipeService.getCalendar(any())).willReturn(calendar);
        given(recipeService.getStats(any())).willReturn(new RecipeStats());

        given(recipeRepository.getFavourite(any(), any())).willReturn(Arrays.asList(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 2;
            }
        }));

        testPagesService.getPersonal(1);

        verify(recipeRepository).getFavourite(1, PageRequest.of(0, 6));
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
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        Map<String, Day> calendar = new HashMap<>();
        calendar.put("monday", new Day(LocalDate.of(2022, 12, 12), 1, true, false));

        given(recipeService.getCalendar(any())).willReturn(calendar);

        testPagesService.getTodays(1);

        verify(recipeRepository).findById(1);
    }

    @Test
    void getCalendarPageThrowsWithNoAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getTodays(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no account with id");
    }
}
