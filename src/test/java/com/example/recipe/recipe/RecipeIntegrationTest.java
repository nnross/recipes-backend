package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

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

    //TODO: just a template
    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    //TODO: just a template
    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    //TODO: just a template
    //TODO: CORRECT URL
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("RECIPE API URL",
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
                .jsonPath("$.title").isEqualTo("test")
                .jsonPath("$.id").isEqualTo("test");
                //TODO: rest
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

        webClient.get().uri("/api/recipe/get/db?recipeId=1")
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
        webClient.get().uri("/api/recipe/get/search?search=test&sort=dateAsc&filter={country=[italy]}")
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
        webClient.get().uri("/api/recipe/get/date?date=2022/12/12")
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
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.post().uri("/api/recipe/favourite?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void doLaterRecipeWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.post().uri("/api/recipe/doLater?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void deleteRecipeWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.delete().uri("/api/recipe/delete?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }
}
