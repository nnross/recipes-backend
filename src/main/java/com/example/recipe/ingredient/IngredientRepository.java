package com.example.recipe.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the ingredient entity.
 */
@Repository
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Integer>, JpaRepository<Ingredient, Integer> {
}