package com.example.recipe.recipe;


import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.response.StatRes;
import com.example.recipe.type.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository testRecipeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void deleteAll() {
        testRecipeRepository.deleteAll();
    }

    @Test
    void RecipeFindByFavouriteWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test fav",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                true,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        PageRequest page = PageRequest.of(0, 4);

        List<ListRecipeRes> foundEntity = testRecipeRepository.getFavourite(account.getId(), page);
        List<ListRecipeRes> foundNoneEntity = testRecipeRepository.getFavourite(0, page);
        assertNotNull(foundEntity);
        assertEquals("test fav", foundEntity.get(0).getTitle());
        assertEquals(1, foundEntity.get(0).getId());
        assertEquals(0, foundNoneEntity.size());
    }

    @Test
    void RecipeFindByDoLaterWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test doLater",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                true,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        PageRequest page = PageRequest.of(0, 4);

        List<ListRecipeRes> foundEntity = testRecipeRepository.getDoLater(account.getId(), page);
        List<ListRecipeRes> foundNoneEntity = testRecipeRepository.getDoLater(0, page);
        assertNotNull(foundEntity);
        assertEquals("test doLater", foundEntity.get(0).getTitle());
        assertEquals(1, foundEntity.get(0).getId());
        assertEquals(0, foundNoneEntity.size());
    }

    @Test
    void RecipeFindByDateWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        PageRequest page = PageRequest.of(0, 4);

        Recipe foundEntity = testRecipeRepository.getByDate(account.getId(), new Date(2022, 12, 12)).orElse(null);
        Recipe foundNoneEntity = testRecipeRepository.getByDate(0, new Date(2020, 10, 12)).orElse(null);
        assertEquals("test title", foundEntity.getTitle());
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }

    @Test
    void RecipeGetStatsWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Country country = countryRepository.findById(1).orElse(null);
        Country country2 = countryRepository.findById(2).orElse(null);
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(country),
                Arrays.asList(new Measurement())
        );
        Recipe recipe2 = new Recipe(
                2,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                true,
                true,
                true,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(country2),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        List<StatRes> foundEntity = testRecipeRepository.getStats(account.getId());
        List<StatRes> foundNoneEntity = testRecipeRepository.getStats(0);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity.get(0).getCount());
        assertEquals("test country", foundEntity.get(0).getName());
        assertEquals(1, foundEntity.get(1).getCount());
        assertEquals("test country2", foundEntity.get(1).getName());
        assertEquals(0, foundNoneEntity.size());
        
    }

    @Test
    void recipeGetDoneCountWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                true,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );
        Recipe recipe2 = new Recipe(
                2,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getDoneCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getDoneCount(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity);
        assertEquals(0, foundNoneEntity);
    }

    @Test
    void recipeGetFavouriteCountWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );
        Recipe recipe2 = new Recipe(
                2,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                true,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getFavouriteCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getFavouriteCount(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity);
        assertEquals(0, foundNoneEntity);
    }

    @Test
    void recipeGetDoLaterCountWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                true,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );
        Recipe recipe2 = new Recipe(
                2,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                false,
                false,
                false,
                new Date(2022, 12, 12),
                "test instructions",
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                account,
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getDoLaterCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getDoLaterCount(0).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(1, foundEntity);
        assertEquals(0, foundNoneEntity);
    }
}
