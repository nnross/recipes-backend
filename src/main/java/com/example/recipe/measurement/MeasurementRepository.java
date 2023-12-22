package com.example.recipe.measurement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the measurement entity.
 */
@Repository
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Integer>, JpaRepository<Measurement, Integer> {
}