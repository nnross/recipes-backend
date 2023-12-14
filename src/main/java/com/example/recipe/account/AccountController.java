package com.example.recipe.account;

import com.example.recipe.response.AuthRes;
import com.example.recipe.security.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the account calls.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * API GET call to /api/account/get?accountId=(id)
     * Takes an id and returns an account with that id.
     * @param id
     *        Id of the account wanted
     * @return the account with the given id
     */
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/get")
    public Account get(@RequestParam("accountId") int id) {
        return accountService.getAccount(id);
    }

    /**
     * API POST call to /api/account/login
     * with account in body
     * Calls the AccountService to log in the user.
     * @param request
     *        AuthRequest with username and password
     * @return AuthRes with token and account id
     */
    @PostMapping("/login")
    public AuthRes login(@RequestBody AuthRequest request) {
        return accountService.login(request);
    }

    /**
     * POST API call to /api/account/create
     * with account in body
     * Creates a new account
     * @param account
     *       New account's name, username, email and password
     * @return AuthRes with token and account id
     */
    @PostMapping("/create")
    public AuthRes create(@RequestBody Account account) { return accountService.create(account); }

    /**
     * API DELETE call to /api/account/delete?accountId=(id)
     * @param id
     *       Id of the account to be deleted
     * @return true if deleted, false if failed
     */
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("accountId") int id) { return accountService.delete(id); }

    /**
     * API PUT call to /api/account/update
     * with account in body
     * @param account
     *       Updated account name, username, email, password
     * @param id
     *       Id of the account being updated
     * @return true id updated, false if failed
     */
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/update")
    public Boolean update(@RequestBody Account account, @RequestParam("accountId") int id) {
        return accountService.update(account, id);
    }
}