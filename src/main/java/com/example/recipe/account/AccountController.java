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
@RequestMapping("/account")
@SuppressWarnings("unused")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * API GET call to /account/get?accountId=(id)
     * Takes an id and returns an account with that id.
     * Authorization with accountId being same as in context.
     * @param id
     *        id of the account wanted
     * @return the account with the given id
     */
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/get")
    public Account get(@RequestParam("accountId") int id) {
        return accountService.getAccount(id);
    }

    /**
     * API POST call to /account/login
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
     * POST API call to /account/create
     * with account in body
     * Creates a new account
     * @param account
     *        New account's name, username, email and password
     * @return AuthRes with token and account id
     */
    @PostMapping("/create")
    public AuthRes create(@RequestBody Account account) { return accountService.create(account); }

    /**
     * API DELETE call to /account/delete?accountId=(id)
     * Authorization with accountId is same as in context
     * @param id
     *       id of the account to be deleted
     * @return true if deleted, error otherwise.
     */
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("accountId") int id) { return accountService.delete(id); }

    /**
     * API PUT call to /account/update?accountId=(id)
     * with account in body
     * Authorization with accountId being same as in context.
     * @param account
     *        Updated account name, username, email, password
     * @param id
     *        id of the account being updated
     * @return true if updated, otherwise error.
     */
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/update")
    public Boolean update(@RequestBody UpdateAccount account, @RequestParam("accountId") int id) {
        return accountService.update(account, id);
    }
}