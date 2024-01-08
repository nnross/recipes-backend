package com.example.recipe.recipe;


import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.instructions.Instruction;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.response.StatRes;
import com.example.recipe.type.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeRepositoryTest {

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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
        );

        testRecipeRepository.save(recipe);

        Recipe foundEntity = testRecipeRepository.getByDate(account.getId(), LocalDate.of(2022,12,12)).orElse(null);
        Recipe foundNoneEntity = testRecipeRepository.getByDate(0, LocalDate.of(2022,12,10)).orElse(null);
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                Collections.singletonList(country),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                Collections.singletonList(country2),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getDoneCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getDoneCount(0).orElse(null);
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getFavouriteCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getFavouriteCount(0).orElse(null);
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
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
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
        );

        testRecipeRepository.save(recipe);
        testRecipeRepository.save(recipe2);

        int foundEntity = testRecipeRepository.getDoLaterCount(account.getId()).orElse(null);
        int foundNoneEntity = testRecipeRepository.getDoLaterCount(0).orElse(null);
        assertEquals(1, foundEntity);
        assertEquals(0, foundNoneEntity);
    }

    @Test
    void getAllForAccountWorks() {
        Account account = new Account(1, "test", "test", "test", "test");
        Recipe recipe = new Recipe(
                1,
                "test",
                "test desc",
                "test original",
                12,
                2,
                "test img",
                100,
                true,
                false,
                false,
                LocalDate.of(2022,12,12),
                List.of(new Instruction("test instructions")),
                List.of(new Category()),
                List.of(new Type()),
                account,
                List.of(new Country()),
                List.of(new Measurement())
        );

        testRecipeRepository.save(recipe);

        List<Recipe> foundEntity = testRecipeRepository.getAllForAccount(account.getId());
        List<Recipe> foundNoneEntity = testRecipeRepository.getAllForAccount(0);

        assertEquals("test", foundEntity.get(0).getTitle());
        assertEquals(1, foundEntity.get(0).getId());
        assertEquals(0, foundNoneEntity.size());
    }
}
