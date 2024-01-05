package com.example.recipe.type;

import com.example.recipe.response.StatRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the type entity.
 */
@Repository
public interface TypeRepository extends PagingAndSortingRepository<Type, Integer>, JpaRepository<Type, Integer> {

    @Query(value = "SELECT * FROM type t WHERE t.type_name = ?1", nativeQuery = true)
    Optional<Type> getTypeByName(String name);
}