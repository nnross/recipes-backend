package com.example.recipe.account;

import com.example.recipe.RecipeApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    @Autowired
    private AccountRepository testAccountRepository;
    @AfterEach
    void deleteAll() {
        testAccountRepository.deleteAll();
    }

    @Test
    void AccountFindByEmailWorks() {
        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        testAccountRepository.save(account);

        Account foundEntity = testAccountRepository.findByEmail(account.getEmail()).orElse(null);
        Account foundNoneEntity = testAccountRepository.findByEmail("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }

    @Test
    void AccountFindByUsernameWorks() {
        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        testAccountRepository.save(account);

        Account foundEntity = testAccountRepository.findByUsername(account.getUsername()).orElse(null);
        Account foundNoneEntity = testAccountRepository.findByUsername("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }
}
