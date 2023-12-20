package com.example.recipe.pages;

import com.example.recipe.account.AccountRepository;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeService;
import com.example.recipe.recipe.RecipeStats;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.PersonalPageRes;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PagesService {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AccountRepository accountRepository;


    /**
     * Gets the personal page data and constucts a PersonalPageRes.
     * @param accountId
     *        id of account we want data for.
     * @return personal page data as PersonalPageRes.
     */
    public PersonalPageRes getPersonal(int accountId) {
        accountRepository.findById(accountId).orElseThrow(() ->
                new BadRequestException("no account with id"));

        ListRes recipes = recipeService.getFavourite(accountId, 0);
        RecipeStats stats = recipeService.getStats(accountId);
        Map<String, Day> calendar = recipeService.getCalendar(accountId);

        return new PersonalPageRes(recipes, stats, calendar);
    };
}
