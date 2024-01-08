package com.example.recipe.ingredient;

import com.example.recipe.RecipeApplication;
import com.example.recipe.type.Type;
import com.example.recipe.type.TypeRepository;
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
public class IngredientRepositoryTest {
    @Autowired
    private IngredientRepository testIngredientRepository;

    @AfterEach
    void deleteAll() {
        testIngredientRepository.deleteAll();
    }

    @Test
    void getIngredientByNameWorks() {
        Ingredient ingredient = new Ingredient(1, "test");
        testIngredientRepository.save(ingredient);

        Ingredient foundEntity = testIngredientRepository.getIngredientByName(ingredient.getName()).orElse(null);
        Ingredient foundNoneEntity = testIngredientRepository.getIngredientByName("not").orElse(null);
        assertNotNull(foundEntity);
        assertEquals("test", foundEntity.getName());
        assertEquals(1, foundEntity.getId());
        assertNull(foundNoneEntity);
    }
}
