package com.example.recipe.category;

import com.example.recipe.RecipeApplication;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
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
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository testCategoryRepository;

    @AfterEach
    void deleteAll() {
        testCategoryRepository.deleteAll();
    }

    @Test
    void getCategoryByName() {
        Category category = new Category(1, "test");
        testCategoryRepository.save(category);

        Category foundEntity = testCategoryRepository.getCategoryByName(category.getName()).orElse(null);
        Category foundNoneEntity = testCategoryRepository.getCategoryByName("not").orElse(null);
        assertNotNull(foundEntity);
        assertEquals("test", foundEntity.getName());
        assertEquals(1, foundEntity.getId());
        assertNull(foundNoneEntity);
    }
}
