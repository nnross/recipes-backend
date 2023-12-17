package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.security.JwtService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="secret.key=cHJvcGVydHlzdWJzdGFuY2V3aXRocmlkaW5nZ3JlYXRhcnRpY2xld2l0aGluZGlzYXA")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeIntegrationTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private WebTestClient webClient;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("requestUrl",
                () -> mockWebServer.url("/").toString());
    }

    @Test
    void getRecipeFromDBWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/db?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test title")
                .jsonPath("$.id").isEqualTo("1");
    }

    @Test
    void getRecipeFromAPIWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(
                                """
                                {
                                  "recipe": [
                                    {
                                      TODO: RECIPE DATA
                                    }
                                  ]
                                }
                                """
                        )
        );

        webClient.get().uri("/api/recipe/get/api/id?id=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test")
                .jsonPath("$.id").isEqualTo("test");
        //TODO: rest
    }
    @Test
    void searchRecipeWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(
                                """
                                {
                                  "recipe": [
                                    {
                                      TODO: RECIPE DATA
                                    }
                                  ]
                                }
                                """
                        )
        );

        // TODO: Actual URL.
        webClient.get().uri("/api/recipe/get/api/search")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$res[0].title").isEqualTo("test")
                .jsonPath("$res[0].id").isEqualTo("test");
        //TODO: rest
    }

    @Test
    void getFavouriteWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/favourite?accountId=1&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$res[0].title").isEqualTo("test")
                .jsonPath("$res[0].id").isEqualTo("test");
        //TODO: rest
    }
    @Test
    void getDoLaterWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/doLater?accountId=1&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$res[0].title").isEqualTo("test")
                .jsonPath("$res[0].id").isEqualTo("test");
        //TODO: rest
    }
    @Test
    void getRecipeByDateWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        //TODO: actual url
        webClient.get().uri("/api/recipe/get/date?accountId=1&date=2022-12-12")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test")
                .jsonPath("$.id").isEqualTo("test");
        //TODO: rest
    }

    @Test
    void favouriteRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFavourite().booleanValue(), false);
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/favourite?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFavourite().booleanValue(), true);

    }
    @Test
    void doLaterRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getDoLater().booleanValue(), false);
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/doLater?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getDoLater().booleanValue(), true);
    }
    @Test
    void finishRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFinished().booleanValue(), false);
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/finished?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFinished().booleanValue(), true);
    }
    @Test
    void deleteRecipeWorks() {
        int res = recipeRepository.findAll().size();
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.delete().uri("/api/recipe/del?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        assertEquals(recipeRepository.findAll().size(), res + 1);
    }
    @Test
    void addRecipeWorks() {
        int res = recipeRepository.findAll().size();
        webClient.post().uri("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
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
                                                        "ingredient": {"id": 1},
                                                        "amount": 12
                                                    }
                                                ]
                                        }
                                """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();

        assertEquals(recipeRepository.findAll().size(), res + 1);
    }
}
