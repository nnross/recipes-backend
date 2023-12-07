package com.example.recipe.account;


import com.example.recipe.RecipeApplication;
import com.example.recipe.security.AuthRequest;
import com.example.recipe.security.JwtService;
import exceptions.BadRequestException;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


// TODO: annotations
@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
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

        Account account = new Account(1, "test name", "test username", "testEmail", "testPass");
        AuthRequest request = new AuthRequest(
                "test",
                "test"
        );

        assertThatThrownBy(() -> testAccountService.login(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");

        verify(accountRepository).findByUsername("test");
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
    void deleteAccountWorks() {

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        testAccountService.delete(0);

        verify(accountRepository).delete(any());
    }

    @Test
    void deleteAccountThrowsWithFailureToDeleteAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        doThrow(new RuntimeException("error")).when(accountRepository).delete(any());

        assertThatThrownBy(() ->  testAccountService.delete(0))
                .isInstanceOf(RuntimeException.class)
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