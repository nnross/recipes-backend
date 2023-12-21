package com.example.recipe.pages;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeRepository;
import com.example.recipe.recipe.RecipeService;
import com.example.recipe.security.JwtService;
import com.example.recipe.type.Type;
import com.example.recipe.unit.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="secret.key=cHJvcGVydHlzdWJzdGFuY2V3aXRocmlkaW5nZ3JlYXRhcnRpY2xld2l0aGluZGlzYXA")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PagesIntegrationTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getPersonalWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/pages/get/personal?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipes.recipes[0].title").isEqualTo("test title 2")
                .jsonPath("$.stats.done").isEqualTo(1);
    }

    @Test
    void getCalendarWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );

        recipeRepository.save(new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                Date.valueOf(LocalDate.now()),
                "test instructions",
                Arrays.asList(new Category(1, "test")),
                Arrays.asList(new Type(1, "test")),
                new Account(1, "test", "test", "test", "test"),
                Arrays.asList(new Country(1, "test")),
                Arrays.asList(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 1F))
        ));

        String token = jwtService.newToken(account);
        webClient.get().uri("/api/pages/get/todays?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipe.title").isEqualTo("test title")
                .jsonPath("$.calendar.Monday.isRecipe").isEqualTo(false);
    }
}
