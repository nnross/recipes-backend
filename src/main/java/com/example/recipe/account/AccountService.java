package com.example.recipe.account;

import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(int id) {
        Account account = accountRepository.findById(id).orElseThrow(()->
                new BadRequestException("Account not found"));
        account.setPassword(null);
        return account;
    }
}
