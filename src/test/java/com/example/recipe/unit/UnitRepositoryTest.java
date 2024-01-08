package com.example.recipe.unit;

import com.example.recipe.RecipeApplication;
import com.example.recipe.recipe.ListRecipeRes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UnitRepositoryTest {
    @Autowired
    private UnitRepository testUnitRepository;

    @AfterEach
    void deleteAll() {
        testUnitRepository.deleteAll();
    }

    @Test
    void getUnitByNameWorks() {
        Unit unit = new Unit(1, "test");
        testUnitRepository.save(unit);

        Unit foundEntity = testUnitRepository.getUnitByName(unit.getName()).orElse(null);
        Unit foundNoneEntity = testUnitRepository.getUnitByName("not").orElse(null);
        assertNotNull(foundEntity);
        assertEquals("test", foundEntity.getName());
        assertEquals(1, foundEntity.getId());
        assertNull(foundNoneEntity);
    }
}
