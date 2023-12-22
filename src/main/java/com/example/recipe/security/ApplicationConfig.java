package com.example.recipe.security;

import com.example.recipe.account.AccountRepository;
import exceptions.ForbiddenException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration for the spring security application
 */
@SuppressWarnings("unused")
@Configuration
public class ApplicationConfig {

    private final AccountRepository accountRepository;

    public ApplicationConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Tells the spring security to search user with username from the database.
     * @return found user.
     */
    @Bean
    public UserDetailsService userDetailsService ()  {
        return username -> accountRepository.findByUsername(username).orElseThrow(
                () -> new ForbiddenException(
                        "Access Denied"
                ));
    }

    /**
     * Gets the authenticationManager from the config.
     * @param authConfig
     *        The AuthenticationConfiguration to be used.
     * @return the authenticationManager from the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Creates a new BcryptPasswordEncoder.
     * @return created passwordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * creates spring security authenticationProvider setUserDetailsService and setPasswordEncoder as our custom.
     * @return created authenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}