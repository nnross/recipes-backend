package com.example.recipe.recipe;

import com.example.recipe.response.StatRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for the recipes.
 */
@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer>, JpaRepository<Recipe, Integer> {

    /**
     * Query to get count of all recipe countries for account.
     * @param accountId
     *        id for account
     * @return List of ResStats with the count of recipes and name of country.
     */
    @Query(value = "SELECT COUNT(recipe_country) AS count, c.country_name AS name FROM recipe_country_mapping m LEFT JOIN country c ON m.country_id = c.country_id LEFT JOIN recipe r ON r.recipe_id = m.recipe_country WHERE r.recipe_account = ?1 GROUP BY c.country_name", nativeQuery = true)
    List<StatRes> getStats(int accountId);

    /**
     * Query to get count of all done recipes
     * @param accountId
     *        id for account
     * @return Count of done recipes
     */
    @Query(value = "SELECT COUNT(*) FROM recipe r WHERE r.recipe_account = ?1 AND r.recipe_finished = 1", nativeQuery = true)
    Optional<Integer> getDoneCount(int accountId);

    /**
     * Query to get count of all favourite recipes
     * @param accountId
     *        id for account
     * @return Count of favourites
     */
    @Query(value = "SELECT COUNT(*) FROM recipe r WHERE r.recipe_account = ?1 AND r.recipe_favourite = 1", nativeQuery = true)
    Optional<Integer> getFavouriteCount(int accountId);

    /**
     * Query to get count of all do Later recipes
     * @param accountId
     *        id for account
     * @return Count of doLater recipes
     */
    @Query(value = "SELECT COUNT(*) FROM recipe r WHERE r.recipe_account = ?1 AND recipe_do_later = 1", nativeQuery = true)
    Optional<Integer> getDoLaterCount(int accountId);

    /**
     * Query to get favourite recipes for account
     * @param accountId
     *        id for account
     * @param pageable
     *        PageRequest for page.
     * @return favourite recipes for account with page.
     */
    @Query(value = "SELECT recipe_title AS title, recipe_id AS id FROM recipe r WHERE r.recipe_account = ?1 AND r.recipe_favourite = 1", nativeQuery = true)
    List<ListRecipeRes> getFavourite(int accountId, Pageable pageable);

    /**
     * Query to get doLater recipes for account
     * @param accountId
     *        id for account
     * @param pageable
     *        PageRequest for page.
     * @return doLater recipes for account with page.
     */
    @Query(value = "SELECT recipe_title AS title, recipe_id AS id FROM recipe r WHERE r.recipe_account = ?1 AND r.recipe_do_later = 1", nativeQuery = true)
    List<ListRecipeRes> getDoLater(int accountId, Pageable pageable);


    /**
     * Query to get recipe for account with date
     * @param accountId
     *        id for account
     * @param date
     *        wanted date for recipe
     * @return recipe with selected date.
     */
    @Query(value = "SELECT * FROM recipe r WHERE r.recipe_account = ?1 AND r.recipe_to_do_date = ?2", nativeQuery = true)
    Optional<Recipe> getByDate(int accountId, LocalDate date);

    /**
     * Query to get all recipes for account with id
     * @param accountId
     *        id of account we want recipes for.
     * @return All accounts recipes.
     */
    @Query(value = "SELECT * FROM recipe r WHERE r.recipe_account = ?1", nativeQuery = true)
    List<Recipe> getAllForAccount(int accountId);
}
