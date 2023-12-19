package com.example.recipe.pages;

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
public class PagesIntegrationTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;

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
                .jsonPath("$.recipes[0].title").isEqualTo("test title")
                .jsonPath("$.stats.doneCount").isEqualTo(12);
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
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/pages/get/calendar?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipe.title").isEqualTo("test title")
                .jsonPath("$.calendar.monday.id").isEqualTo(1);
    }
}
