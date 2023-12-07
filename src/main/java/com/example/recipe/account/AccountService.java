package com.example.recipe.account;

import com.example.recipe.response.AuthRes;
import com.example.recipe.security.AuthRequest;
import com.example.recipe.security.JwtService;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Logic for account calls.
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

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
     *       Id of the account to get
     * @return An account with the id
     */
    public Account getAccount(int id) {
        Account account = accountRepository.findById(id).orElseThrow(()->
                new BadRequestException("Account not found"));
        account.setPassword(null);
        return account;
    }

    /**
     * Logs user in.
     * @param request
     *       AuthRequest of username and password
     * @return AuthRes of token and user's id
     */
    public AuthRes login(AuthRequest request) {
        System.out.println(passwordEncoder.encode(request.getPassword()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.newToken(account);
        return new AuthRes(token, account.getId());
    }

    /**
     * Creates a new account.
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
            throw new RuntimeException("Failed to save to database");
        }

        String token = jwtService.newToken(account);
        return new AuthRes(token, account.getId());
    }

    /**
     * Deletes an account.
     * @param id
     *       Id of the account to be deleted
     * @return true if account was deleted, false if not
     */
    public Boolean delete(int id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new BadRequestException("No accounts with the id"));
        // TODO: delete all account's recipies
        try {
            accountRepository.delete(account);
        } catch(Exception e) {
            throw new RuntimeException("Failed to delete account");
        }
        return true;
    }

    /**
     * Updates user's account information.
     * @param account
     *      Contains user's new name, username, email and password
     * @param id
     *      Id of the account being updated
     * @return true if account was updated, false if not
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
            throw new RuntimeException("Failed to save changes to database");
        }
        return true;
    }
}
