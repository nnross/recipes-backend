package com.example.recipe.country;

import com.example.recipe.RecipeApplication;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.ingredient.IngredientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository testCountryRepository;

    @AfterEach
    void deleteAll() {
        testCountryRepository.deleteAll();
    }

    @Test
    void getCountryByNameWorks() {
        Country country = new Country(1, "test");
        testCountryRepository.save(country);

        Country foundEntity = testCountryRepository.getCountryByName(country.getName()).orElse(null);
        Country foundNoneEntity = testCountryRepository.getCountryByName("not").orElse(null);
        assertNotNull(foundEntity);
        assertEquals("test", foundEntity.getName());
        assertEquals(1, foundEntity.getId());
        assertNull(foundNoneEntity);
    }
}
