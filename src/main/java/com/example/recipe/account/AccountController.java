package com.example.recipe.account;

import com.example.recipe.response.AuthRes;
import com.example.recipe.security.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/get")
    public Account get(@RequestParam("accountId") int id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/login")
    public AuthRes login(@RequestBody AuthRequest request) {
        return accountService.login(request);
    }

    @PostMapping("/create")
    public AuthRes create(@RequestBody Account account) { return accountService.create(account); }
}