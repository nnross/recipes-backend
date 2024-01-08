package com.example.recipe.type;

import com.example.recipe.RecipeApplication;
import com.example.recipe.unit.Unit;
import com.example.recipe.unit.UnitRepository;
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
public class TypeRepositoryTest {
    @Autowired
    private TypeRepository testTypeRepository;

    @AfterEach
    void deleteAll() {
        testTypeRepository.deleteAll();
    }

    @Test
    void getTypeByNameWorks() {
        Type type = new Type(1, "test");
        testTypeRepository.save(type);

        Type foundEntity = testTypeRepository.getTypeByName(type.getName()).orElse(null);
        Type foundNoneEntity = testTypeRepository.getTypeByName("not").orElse(null);
        assertNotNull(foundEntity);
        assertEquals("test", foundEntity.getName());
        assertEquals(1, foundEntity.getId());
        assertNull(foundNoneEntity);
    }
}
