package com.example.recipe.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: annotations
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountIntegrationTest {
    //TODO: mocks

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAccountWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/account/get?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("initSeller name")
                .jsonPath("$.username").isEqualTo("initSeller")
                .jsonPath("$.password").isEqualTo(null)
                .jsonPath("$.email").isEqualTo("initSeller email");
    }
    @Test
    void addAccountWorks() {
        int items = accountRepository.findAll().size();
        webClient.post().uri("/api/account/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "name": "test",
                            "username": "testUsername",
                            "password": "testPass123!",
                            "email": "testEmail"
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
                "test",
                "initSeller",
                "initSeller pass",
                "email"
        );
        String token = jwtService.newToken(account);

        webClient.delete().uri("/api/account/del?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(1, accountRepository.findAll().size());

    }

    @Test
    void loginWorks() {
        webClient.post().uri("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "username": "initSeller",
                            "password": "adminPass123!"
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
                "test",
                "initSeller",
                "initSeller pass",
                "email"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/account/update?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(
                        """
                        {
                            "name": "newName",
                            "username": "newUser",
                            "password": "newPass1!",
                            "email": "newEmail"
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");
    }
}