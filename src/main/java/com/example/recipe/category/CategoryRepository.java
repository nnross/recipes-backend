package com.example.recipe.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for category.
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, JpaRepository<Category, Integer> {
}
