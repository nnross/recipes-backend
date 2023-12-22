package com.example.recipe.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the country entity.
 */
@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Integer>, JpaRepository<Country, Integer> {
}