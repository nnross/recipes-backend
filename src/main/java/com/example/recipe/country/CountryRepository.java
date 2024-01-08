package com.example.recipe.country;

import com.example.recipe.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the country entity.
 */
@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Integer>, JpaRepository<Country, Integer> {

    /**
     * Query to get the country by name
     * @param name
     *        name of the country wanted
     * @return country by name
     */
    @Query(value = "SELECT * FROM country c WHERE c.country_name = ?1", nativeQuery = true)
    Optional<Country> getCountryByName(String name);
}