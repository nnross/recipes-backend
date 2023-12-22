package com.example.recipe.account;

import com.example.recipe.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="secret.key=cHJvcGVydHlzdWJzdGFuY2V3aXRocmlkaW5nZ3JlYXRhcnRpY2xld2l0aGluZGlzYXA")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountIntegrationTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAccountWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/account/get?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("test name")
                .jsonPath("$.username").isEqualTo("test username")
                .jsonPath("$.password").isEqualTo(null)
                .jsonPath("$.email").isEqualTo("test email");
    }
    @Test
    void addAccountWorks() {
        int items = accountRepository.findAll().size();
        webClient.post().uri("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "name": "test name",
                            "username": "testUsername",
                            "email": "testEmail",
                            "password": "testPass123!"
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();

        assertEquals(accountRepository.findAll().size(), items + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteAccountWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "testPass123!"
        );
        String token = jwtService.newToken(account);

        webClient.delete().uri("/account/delete?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(0, accountRepository.findAll().size());

    }

    @Test
    void loginWorks() {
        webClient.post().uri("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "username": "test username",
                            "password": "test"
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();
    }

    @Test
    void updateAccountWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/account/update?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(
                        """
                        {
                            "name": "newName",
                            "username": "newUser",
                            "email": "test email",
                            "password": "testPass123!"
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");
    }
}