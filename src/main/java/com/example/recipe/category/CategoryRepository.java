package com.example.recipe.category;

import com.example.recipe.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, JpaRepository<Category, Integer> {
}
