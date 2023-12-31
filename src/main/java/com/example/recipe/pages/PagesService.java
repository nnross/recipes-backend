package com.example.recipe.pages;

import com.example.recipe.account.AccountRepository;
import com.example.recipe.recipe.Day;
import com.example.recipe.recipe.RecipeService;
import com.example.recipe.recipe.RecipeStats;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.ListRes;
import com.example.recipe.response.PersonalPageRes;
import com.example.recipe.response.TodaysPageRes;
import exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

/**
 * Logic for the page calls.
 */
@Service
@SuppressWarnings("unused")
public class PagesService {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AccountRepository accountRepository;


    /**
     * Gets the personal page data and constructs a PersonalPageRes.
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
    }

    /**
     * Gets the today's page data and constructs a TodaysPageRes.
     * @param accountId
     *        id of account we want data for.
     * @return personal page data as PersonalPageRes.
     */
    public TodaysPageRes getTodays(int accountId, String date) {
        accountRepository.findById(accountId).orElseThrow(() ->
                new BadRequestException("no account with id"));
        LocalDate dateToLocal = LocalDate.parse(date);
        FullRecipeRes recipe = recipeService.getRecipeForDate(accountId, dateToLocal);
        Map<String, Day> calendar = recipeService.getCalendar(accountId);

        return new TodaysPageRes(recipe, calendar);
    }
}
