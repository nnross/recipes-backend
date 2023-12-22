package com.example.recipe.account;

import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeRepository;
import com.example.recipe.response.AuthRes;
import com.example.recipe.security.AuthRequest;
import com.example.recipe.security.JwtService;
import exceptions.BadRequestException;
import exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Logic for account calls.
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds the account with the given id.
     * @param id
     *       id of the account to get
     * @return An account with the id
     */
    public Account getAccount(int id) {
        Account account = accountRepository.findById(id).orElseThrow(()->
                new BadRequestException("Account not found"));
        account.setPassword(null);
        return account;
    }

    /**
     * Logs user in to the application and creates a token for login.
     * @param request
     *       AuthRequest of username and password
     * @return AuthRes of token and user's id
     */
    public AuthRes login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new BadRequestException("Invalid username")
        );
        String token = jwtService.newToken(account);
        return new AuthRes(token, account.getId());
    }

    /**
     * Creates a new account and creates a token for the account.
     * @param account
     *       Account with name, username, email and password for new user
     * @return AuthRes of token and user's id
     */
    public AuthRes create(Account account) {
        if (accountRepository.findByUsername(account.getUsername()).isPresent()
            || !account.getUsername().matches("^(.{4,20})")) {
            throw new BadRequestException("Invalid username");
        }
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new BadRequestException("Invalid email");
        }
        if (!account.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new BadRequestException("Invalid password");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        try {
            accountRepository.save(account);
        } catch(Exception e) {
            throw new DatabaseException("Failed to save to database");
        }

        String token = jwtService.newToken(account);
        return new AuthRes(token, account.getId());
    }

    /**
     * Deletes an account.
     * @param id
     *        id of the account to be deleted
     * @return true if account was deleted, error otherwise.
     */
    public Boolean delete(int id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new BadRequestException("No accounts with the id"));

        List<Recipe> recipes = recipeRepository.getAllForAccount(id);
        for (Recipe recipe : recipes) {
            recipe.getCountry().clear();
            recipe.getType().clear();
            recipe.getCategory().clear();
        }

        try {
            accountRepository.delete(account);
        }
        catch (Exception e) {
            throw new DatabaseException("Failed to delete account");
        }

        return true;
    }

    /**
     * Updates user's account information.
     * @param account
     *        Contains user's new name, username, email and password
     * @param id
     *        id of the account being updated
     * @return true if account was updated, error otherwise.
     */
    public Boolean update(Account account, int id) {
        Account oldAccount = accountRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Invalid id"));

        if ((accountRepository.findByUsername(account.getUsername()).isPresent()
                && !oldAccount.getUsername().equals(account.getUsername()))
                || !account.getUsername().matches("^(.{4,20})")) {
            throw new BadRequestException("Invalid username");
        }
        if (accountRepository.findByEmail(account.getEmail()).isPresent()
            && !oldAccount.getEmail().equals(account.getEmail())) {
            throw new BadRequestException("Invalid email");
        }
        if (!account.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
            && !account.getPassword().equals("null")) {
            throw new BadRequestException("Invalid password");
        }

        oldAccount.setName(account.getName());
        oldAccount.setUsername(account.getUsername());
        oldAccount.setEmail(account.getEmail());
        if (!account.getPassword().equals("null")) {
            oldAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        try {
            accountRepository.save(oldAccount);
        } catch(Exception e) {
            throw new DatabaseException("Failed to save changes to database");
        }
        return true;
    }
}
