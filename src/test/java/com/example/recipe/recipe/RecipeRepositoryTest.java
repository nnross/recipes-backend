package com.example.recipe.recipe;


import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository testRecipeRepository;
    @AfterEach
    void deleteAll() {
        testRecipeRepository.deleteAll();
    }

    @Test
    void RecipeFindByFavouriteWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);

        List<Recipe> foundEntity = testRecipeRepository.findByFavourite(account.getId()).orElse(null);
        List<Recipe> foundNoneEntity = testRecipeRepository.findByFavourite(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals("title", Recipe[0].title);
        assertNull(foundNoneEntity);
    }

    @Test
    void RecipeFindByDoLaterWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);

        List<Recipe> foundEntity = testRecipeRepository.findByDoLater(account.getId()).orElse(null);
        List<Recipe> foundNoneEntity = testRecipeRepository.findByDoLater(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals("title", Recipe[0].title);
        assertNull(foundNoneEntity);
    }

    @Test
    void RecipeFindByDateWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);

        List<Recipe> foundEntity = testRecipeRepository.findByDate(account.getId(), new Date(2020, 10, 12)).orElse(null);
        List<Recipe> foundNoneEntity = testRecipeRepository.findByDate(0, new Date(2020, 10, 12)).orElse(null);
        assertEquals("title", Recipe[0].title);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }

    @Test
    void RecipeFindCountryByFinishedWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );
        Recipe recipe2 = new Recipe(
                2,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        List<Stats> foundEntity = testRecipeRepository.getCountryForFinished(account.getId()).orElse(null);
        List<Stats> foundNoneEntity = testRecipeRepository.getCountryForFinished(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity[0].count);
        assertEquals("asian", foundEntity[0].name);
        assertEquals(1, foundEntity[1].count);
        assertNull(foundNoneEntity);
        
    }

    @Test
    void RecipeFindStatisticsByIdWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );
        Recipe recipe2 = new Recipe(
                2,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        List<Stats> foundEntity = testRecipeRepository.getStatsForId(account.getId()).orElse(null);
        List<Stats> foundNoneEntity = testRecipeRepository.getStatsForId(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity[0].count);
        assertEquals("favourite", foundEntity[0].name);
        assertEquals(1, foundEntity[1].count);
        assertEquals("do later", foundEntity[0].name);
        assertEquals(0, foundEntity[2].count);
        assertEquals("finished", foundEntity[0].name);
        assertNull(foundNoneEntity);
    }

    // TODO: not sure if this correct
    @Test
    void RecipeFindCalendarByIdWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );
        Recipe recipe2 = new Recipe(
                2,
                "title",
                new Category(),
                new Types(),
                account,
                "test desc",
                1400,
                5,
                "test image",
                true,
                true,
                false,
                new Country(),
                new Date(),
                new Measurements(),
                new Instructions()
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        List<Calendar> foundEntity = testRecipeRepository.getCalendar(account.getId());
        List<Calendar> foundNoneEntity = testRecipeRepository.getCalendar(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity[0].status);
        assertEquals("monday", foundEntity[0].date);
        assertEquals(2, foundEntity[1].status);
        assertEquals("tuesday", foundEntity[1].date);
        assertEquals(0, foundEntity[2].status);
        assertEquals("wednesday", foundEntity[2].date);
        assertEquals(0, foundEntity[3].status);
        assertEquals("thursday", foundEntity[3].date);
        assertEquals(0, foundEntity[4].status);
        assertEquals("friday", foundEntity[4].date);
        assertEquals(0, foundEntity[5].status);
        assertEquals("saturday", foundEntity[5].date);
        assertEquals(0, foundEntity[6].status);
        assertEquals("sunday", foundEntity[6].date);

        assertNull(foundNoneEntity);
    }
}
