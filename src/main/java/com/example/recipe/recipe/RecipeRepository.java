package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer>, JpaRepository<Recipe, Integer> {
}
