package com.example.recipe.measurement;

import com.example.recipe.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Integer>, JpaRepository<Measurement, Integer> {
}