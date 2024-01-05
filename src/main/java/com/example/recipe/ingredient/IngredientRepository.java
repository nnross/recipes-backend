package com.example.recipe.ingredient;

import com.example.recipe.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the ingredient entity.
 */
@Repository
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Integer>, JpaRepository<Ingredient, Integer> {

    @Query(value = "SELECT * FROM ingredient i WHERE i.ingredient_name = ?1", nativeQuery = true)
    Optional<Ingredient> getIngredientByName(String name);
}