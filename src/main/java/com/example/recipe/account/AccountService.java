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

    public Account getAccount(int id) {
        Account account = accountRepository.findById(id).orElseThrow(()->
                new BadRequestException("Account not found"));
        account.setPassword(null);
        return account;
    }

    public AuthRes login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.newToken(account);
        return new AuthRes(token, account.getId());
    }

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
}
