package com.example.recipe.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the type entity.
 */
@Repository
public interface TypeRepository extends PagingAndSortingRepository<Type, Integer>, JpaRepository<Type, Integer> {

    /**
     * Query to get the type by name
     * @param name
     *        name of the type wanted
     * @return type by name
     */
    @Query(value = "SELECT * FROM type t WHERE t.type_name = ?1", nativeQuery = true)
    Optional<Type> getTypeByName(String name);
}