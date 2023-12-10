package com.example.recipe.ingredient;

import com.example.recipe.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Integer>, JpaRepository<Ingredient, Integer> {
}