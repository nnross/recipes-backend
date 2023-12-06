package com.example.recipe.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PreAuthorize("id == authentication.principal.id")
    @GetMapping("/get")
    public Account get(@RequestParam("accountId") int id) {
        return accountService.getAccount(id);
    }
}