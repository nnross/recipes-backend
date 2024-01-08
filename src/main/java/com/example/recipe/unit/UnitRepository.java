package com.example.recipe.unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the Unit entity.
 */
@Repository
public interface UnitRepository extends PagingAndSortingRepository<Unit, Integer>, JpaRepository<Unit, Integer> {

    /**
     * Query for the unit by name
     * @param name
     *        name of the unit wanted
     * @return unit for the name
     */
    @Query(value = "SELECT * FROM unit u WHERE u.unit_name = ?1", nativeQuery = true)
    Optional<Unit> getUnitByName(String name);
}
