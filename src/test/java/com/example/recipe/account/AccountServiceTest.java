package com.example.recipe.account;


import com.example.recipe.RecipeApplication;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeRepository;
import com.example.recipe.security.AuthRequest;
import com.example.recipe.security.JwtService;
import com.example.recipe.type.Type;
import com.example.recipe.unit.Unit;
import exceptions.BadRequestException;
import exceptions.DatabaseException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AccountService testAccountService;

    @Test
    void getAccountWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        testAccountService.getAccount(1);

        verify(accountRepository).findById(1);
    }

    @Test
    void getAccountThrowsWithNoAccount() {
        assertThatThrownBy(() ->  testAccountService.getAccount(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Account not found");

    }

    @Test
    void saveAccount() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        testAccountService.create(account);

        verify(accountRepository).save(account);
    }

    @Test
    void saveAccountThrowsWithBadPasswordCharacter() {

        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!ä"
        );

        assertThatThrownBy(() ->  testAccountService.create(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid password");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAccountThrowsWithUsernameTooShort() {
        Account account = new Account(
                1,
                "tes",
                "user name",
                "example@gmail.com",
                "passWord123!"
        );

        assertThatThrownBy(() -> testAccountService.create(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid username");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAccountThrowsWithTooShortPassword() {
        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "pA23!"
        );

        assertThatThrownBy(() ->  testAccountService.create(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid password");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAccountThrowsWithNotUniqueUsername() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));
        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        assertThatThrownBy(() ->  testAccountService.create(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid username");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAccountThrowsWithNotUniqueEmail() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));
        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        assertThatThrownBy(() ->  testAccountService.create(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid email");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAccountThrowsWithFailureToSaveToDatabase() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());
        given(accountRepository.save(any())).willThrow(new RuntimeException("error"));

        Account account = new Account(
                1,
                "test name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        assertThatThrownBy(() ->  testAccountService.create(account))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to save to database");
    }

    @Test
    void loginWorks() {

        Account account = new Account(1, "test name", "test username", "testEmail", "testPass");
        AuthRequest request = new AuthRequest(
                "test",
                "test"
        );

        given(accountRepository.findByUsername(any())).willReturn(Optional.of(account));
        testAccountService.login(request);

        verify(accountRepository).findByUsername("test");
    }




    @Test
    void loginThrowsWithNoUserInDB() {

        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());

        AuthRequest request = new AuthRequest("test", "test");

        assertThatThrownBy(() -> testAccountService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid username");
    }

    @Test
    void updateAccountWorks() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        testAccountService.update(updateAccount, updateAccount.getId());

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountThrowsWithNoMatchingAccount() {

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "example2@gmail.com",
                "passWord123!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.empty());
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.update(updateAccount, AccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid id");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadEmail() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username2",
                "example2@gmail.com",
                "passWord123!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.update(updateAccount, AccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid email");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadUsername() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username2",
                "example@gmail.com",
                "passWord123!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.update(updateAccount, AccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid username");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadPassword() {

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username2",
                "example2@gmail.com",
                "psas!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.update(updateAccount, AccountId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid password");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountWorksWithSameEmailAndUsername() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "testusername2", "test name", "testEmail", "testPass");
        Account updateAccount = new Account(
                1,
                "testusername2",
                "test name",
                "testEmail",
                "passWord321!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        testAccountService.update(updateAccount, updateAccount.getId());

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountThrowsWithFailureToSaveToDatabase() {
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());
        given(accountRepository.save(any())).willThrow(new RuntimeException("error"));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username",
                "example@gmail.com",
                "passWord123!"
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));

        assertThatThrownBy(() -> testAccountService.update(updateAccount, updateAccount.getId()))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to save changes to database");
    }

    @Test
    void deleteAccountWorks() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "test country"));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "test category"));
        List<Type> types = new ArrayList<>();
        types.add(new Type(1, "test type"));
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12));

        given(recipeRepository.getAllForAccount(anyInt()))
                .willReturn(List.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        true,
                        LocalDate.of(2022,12,12),
                        "test instructions",
                        categories,
                        types,
                        new Account(),
                        countries,
                        measurements
                )));

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        testAccountService.delete(0);

        verify(accountRepository).delete(any());
    }

    @Test
    void deleteAccountThrowsWithFailureToDeleteAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        doThrow(new RuntimeException("error")).when(accountRepository).delete(any());

        assertThatThrownBy(() ->  testAccountService.delete(0))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to delete account");
    }

    @Test
    void deleteAccountThrowsErrorWithNoMatchingAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testAccountService.delete(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No accounts with the id");

        verify(accountRepository, never()).deleteById(0);
    }
}