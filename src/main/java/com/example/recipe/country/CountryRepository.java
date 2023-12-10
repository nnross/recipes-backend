package com.example.recipe.country;

import com.example.recipe.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Integer>, JpaRepository<Country, Integer> {
}