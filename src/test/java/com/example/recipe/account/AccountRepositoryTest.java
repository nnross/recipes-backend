package com.example.recipe.account;

import com.example.recipe.RecipeApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

// TODO: annotations
@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    // TODO: mocks and autowires
    @AfterEach
    void deleteAll() {
        testaccountRepository.deleteAll();
    }

    @Test
    void AccountFindByEmailWorks() {
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com"
        );

        testaccountRepository.save(account);

        Account foundEntity = testaccountRepository.findByEmail(account.getEmail()).orElse(null);
        Account foundNoneEntity = testaccountRepository.findByEmail("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }

    @Test
    void AccountFindByUsernameWorks() {
        Role role = testRoleRepository.findById(1).orElseThrow();
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com"
        );

        testaccountRepository.save(account);

        Account foundEntity = testaccountRepository.findByUsername(account.getUsername()).orElse(null);
        Account foundNoneEntity = testaccountRepository.findByUsername("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }
}
