package com.example.recipe.account;


import com.example.recipe.response.AuthRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: annotations
@WebMvcTest(value= AccountController.class)
@ContextConfiguration(classes = AccountController.class)
@EnableMethodSecurity
class AccountControllerTest {
    @MockBean
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;


    @Test
    void getAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "email",
                "pass"
        );
        given(accountService.getAccount(anyInt())).willReturn(account);

        mockMvc.perform(get("/api/account/get?accountId=1", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getEmail()))
                .andExpect(jsonPath("$.password").value("pass"))
                .andExpect(jsonPath("$.username").value(account.getUsername()));
    }

    @Test
    void getAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail"
        );
        mockMvc.perform(get("/api/account/get").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "email",
                "pass"
        );
        mockMvc.perform(get("/api/account/get?accountId=2", 1).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addAccountWorks() throws Exception {

        given(accountService.create(any())).willReturn(new AuthRes("token", 1));

        mockMvc.perform(post("/api/account/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "test name",
                            "username": "testusername",
                            "email": "test email",
                            "password": "TestPass123!"
                        }
                        """)
                        .with(user(new Account())))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void addAccountThrowsWithNoItemGiven() throws Exception {
        mockMvc.perform(post("/api/account/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(new Account())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWorks() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token", 1));
        mockMvc.perform(post("/api/account/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testusername",
                            "password": "TestPass123!"
                        }
                        """)
                        .with(user(new Account())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void loginThrowsWithNoItem() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token", 1));
        mockMvc.perform(post("/api/account/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(new Account())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );

        given(accountService.update(any(), anyInt())).willReturn(true);

        mockMvc.perform(put("/api/account/update?accountId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "email": "updatedEmail",
                            "pass": "updatedPass"
                        }
                        """)
                        .with(user(account)))

                .andExpect(status().isOk());
    }
    @Test
    void updateAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );

        mockMvc.perform(put("/api/account/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "email": "updatedEmail",
                            "pass": "updatedPass"
                        }
                        """)
                        .with(user(account)))

                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountThrowsWithNoContent() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );

        mockMvc.perform(put("/api/account/update?accountId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail"
        );

        mockMvc.perform(put("/api/account/update?accountId=2").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "password": "updatedPass",
                            "email": "updatedEmail"
                        }
                        """)
                        .with(user(account)))

                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );
        given(accountService.delete(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/delete?accountId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteAccountReturnsFalseIfFails() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );
        given(accountService.delete(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/account/delete?accountId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );
        given(accountService.delete(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/delete").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedEmail",
                "updatedPass"
        );
        given(accountService.delete(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/delete?accountId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}