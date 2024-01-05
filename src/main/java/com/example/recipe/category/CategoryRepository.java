package com.example.recipe.category;

import com.example.recipe.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for category.
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, JpaRepository<Category, Integer> {

    @Query(value = "SELECT * FROM category c WHERE c.category_name = ?1", nativeQuery = true)
    Optional<Category> getCategoryByName(String name);
}
