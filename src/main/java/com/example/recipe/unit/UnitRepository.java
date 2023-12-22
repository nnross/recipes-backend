package com.example.recipe.unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Unit entity.
 */
@Repository
public interface UnitRepository extends PagingAndSortingRepository<Unit, Integer>, JpaRepository<Unit, Integer> {
}
