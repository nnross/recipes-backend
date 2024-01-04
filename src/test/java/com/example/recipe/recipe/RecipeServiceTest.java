package com.example.recipe.recipe;

import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.apiClasses.*;
import com.example.recipe.category.Category;
import com.example.recipe.category.CategoryRepository;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.ingredient.IngredientRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.measurement.MeasurementRepository;
import com.example.recipe.response.FullRecipeRes;
import com.example.recipe.response.RecipeRes;
import com.example.recipe.response.StatRes;
import com.example.recipe.security.JwtService;
import com.example.recipe.type.Type;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.Unit;
import com.example.recipe.unit.UnitRepository;
import exceptions.BadRequestException;
import exceptions.DatabaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = {RecipeApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private RecipeUtils recipeUtils;

    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private RecipeService testRecipeService;

    @Test
    void addRecipeWorks() {
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        given(countryRepository.findById(any())).willReturn(Optional.of(new Country()));
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));

        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category(1, "test")),
                List.of(new Type(1, "test")),
                new Account(1, "test", "test", "test", "test"),
                List.of(new Country(1, "test")),
                List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 1F))
        );

        testRecipeService.add(recipe);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void addRecipeThrowsWithBadUnit() {
        given(unitRepository.findById(any())).willReturn(Optional.empty());
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(), new Ingredient(), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("unit not in database");
    }

    @Test
    void addRecipeThrowsWithIngredient() {
        given(ingredientRepository.findById(any())).willReturn(Optional.empty());
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("ingredient not in database");
    }

    @Test
    void addRecipeThrowsWithBadType() {
        given(typeRepository.findById(any())).willReturn(Optional.empty());
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("type not in database");
    }

    @Test
    void addRecipeThrowsWithBadCountry() {
        given(countryRepository.findById(any())).willReturn(Optional.empty());
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(), new Ingredient(), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("country not in database");
    }

    @Test
    void addRecipeThrowsWithBadCategory() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        given(countryRepository.findById(any())).willReturn(Optional.of(new Country()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(), new Ingredient(), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("category not in database");
    }

    @Test
    void addRecipeThrowsWithErrorWhileSaving() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        given(countryRepository.findById(any())).willReturn(Optional.of(new Country()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        doThrow(new RuntimeException()).when(recipeRepository).save(any());
        Recipe recipe = new Recipe(
                1,
                "test title",
                "test desc",
                "test original",
                12,
                2,
                "test image",
                120,
                false,
                false,
                false,
                null,
                "test instructions",
                List.of(new Category()),
                List.of(new Type()),
                new Account(),
                List.of(new Country()),
                List.of(new Measurement(1, new Unit(), new Ingredient(), 12))
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void toggleFavouriteWorks() {
        Recipe recipe = new Recipe();
        recipe.setFavourite(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));

        testRecipeService.toggleFavourite( 1);
        recipe.setFavourite(false);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void toggleFavouriteThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testRecipeService.toggleFavourite(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void toggleFavouriteThrowsWithErrorWhileSaving() {
        Recipe recipe = new Recipe();
        recipe.setFavourite(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));
        doThrow(new RuntimeException()).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.toggleFavourite(1))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void doLaterRecipeWorks() {
        Recipe recipe = new Recipe();
        recipe.setDoLater(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));

        testRecipeService.toggleDoLater( 1);
        recipe.setDoLater(false);
        verify(recipeRepository).save(recipe);

    }
    @Test
    void toggleDoLaterThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testRecipeService.toggleDoLater(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void toggleDoLaterThrowsWithErrorWhileSaving() {
        Recipe recipe = new Recipe();
        recipe.setDoLater(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));
        doThrow(new RuntimeException()).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.toggleDoLater(1))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void finishRecipeWorks() {
        Recipe recipe = new Recipe();
        recipe.setFinished(false);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));

        testRecipeService.finishRecipe(1);
        recipe.setFinished(true);

        verify(recipeRepository).save(recipe);
    }

    @Test
    void finishRecipeThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testRecipeService.finishRecipe(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void finishRecipeThrowsWithErrorWhileSaving() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        doThrow(new RuntimeException("error")).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.finishRecipe(1))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void searchRecipeWorks() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), anyInt()))
                .willReturn(new RecipeResponse(List.of(new ShortRecipe())));

        testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("vegan"), List.of("dairy"), "main course", "time", "asc", 0);

        verify(recipeUtils).searchResults("search", "pork", "asian", "vegan", "dairy", "main course", "time", "asc", 0);
    }

    @Test
    void searchRecipeThrowsWithBadIngredient() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("bad"), List.of("asian"), List.of("vegan"), List.of("dairy"), "main course", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid ingredient filter");
    }

    @Test
    void searchRecipeThrowsWithBadCuisine() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("bad"), List.of("vegan"), List.of("dairy"), "main course", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid cuisine filter");
    }

    @Test
    void searchRecipeThrowsWithBadDiet() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("bad"), List.of("dairy"), "main course", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid diet filter");
    }

    @Test
    void searchRecipeThrowsWithBadIntolerance() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("vegan"), List.of("bad"), "main course", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid intolerance filter");
    }

    @Test
    void searchRecipeThrowsWithBadType() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("vegan"), List.of("dairy"), "bad", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid type filter");
    }

    @Test
    void searchRecipeThrowsWithBadSort() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("vegan"), List.of("dairy"), "main course", "bad", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid sort");
    }

    @Test
    void searchRecipeThrowsWithBadSortDir() {
        assertThatThrownBy(() -> testRecipeService.getSearch("search", List.of("pork"), List.of("asian"), List.of("vegan"), List.of("dairy"), "main course", "time", "bad", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid sort direction");
    }

    @Test
    void getRecipeFromAPIWorks() {
        given(recipeUtils.getRecipeById(anyInt()))
                .willReturn(new RecipeFormat(
                        1,
                        "title",
                        "image src",
                        2,
                        12,
                        "source",
                        "instructions",
                        "summary",
                        12,
                        true,
                        true,
                        true,
                        true,
                        List.of("main course"),
                        List.of("indian"),
                        List.of("vegan"),
                        List.of(new RecipeIngredients("test name", new Measures(new Metric(2, "tbsp"))))
                ));
        RecipeRes res = testRecipeService.getSearchById(1);

        assertEquals("vegan", res.getDiets().get(0));
        assertEquals("test name", res.getMeasurements().get(0).getName());
        assertEquals(2, res.getMeasurements().get(0).getAmount());
        verify(recipeUtils).getRecipeById(1);
    }

    @Test
    void getStatsForAccountWorks() {
        List<StatRes> list = new ArrayList<>();
        list.add(new StatRes() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public int getCount() {
                return 12;
            }
        });

        given(recipeRepository.getStats(anyInt())).willReturn(list);
        given(recipeRepository.getDoneCount(anyInt())).willReturn(Optional.of(12));
        given(recipeRepository.getFavouriteCount(anyInt())).willReturn(Optional.of(2));
        given(recipeRepository.getDoLaterCount(anyInt())).willReturn(Optional.of(4));

        RecipeStats res = testRecipeService.getStats(1);

        assertEquals(12, res.getChart().get(0).getCount());
        assertEquals(12, res.getDone());
        assertEquals(2, res.getFavourite());
        assertEquals(4, res.getDoLater());
    }

    @Test
    void getStatsForAccountThrowsWithNoDoneCount() {
        List<StatRes> list = new ArrayList<>();
        list.add(new StatRes() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public int getCount() {
                return 12;
            }
        });

        given(recipeRepository.getStats(anyInt())).willReturn(list);
        given(recipeRepository.getDoneCount(anyInt())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testRecipeService.getStats(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while getting done count from database");
    }

    @Test
    void getStatsForAccountThrowsWithNoFavouriteCount() {
        List<StatRes> list = new ArrayList<>();
        list.add(new StatRes() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public int getCount() {
                return 12;
            }
        });

        given(recipeRepository.getStats(anyInt())).willReturn(list);
        given(recipeRepository.getFavouriteCount(anyInt())).willReturn(Optional.empty());
        given(recipeRepository.getDoneCount(anyInt())).willReturn(Optional.of(2));

        assertThatThrownBy(() -> testRecipeService.getStats(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while getting favourite count from database");
    }

    @Test
    void getStatsForAccountThrowsWithNoDoLaterCount() {
        List<StatRes> list = new ArrayList<>();
        list.add(new StatRes() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public int getCount() {
                return 12;
            }
        });

        given(recipeRepository.getStats(anyInt())).willReturn(list);
        given(recipeRepository.getDoLaterCount(anyInt())).willReturn(Optional.empty());
        given(recipeRepository.getDoneCount(anyInt())).willReturn(Optional.of(2));
        given(recipeRepository.getFavouriteCount(anyInt())).willReturn(Optional.of(4));

        assertThatThrownBy(() -> testRecipeService.getStats(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while getting doLater count from database");
    }

    @Test
    void getFavouriteForAccountWorks() {
        given(recipeRepository.getFavourite(anyInt(), any())).willReturn(List.of(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));
        testRecipeService.getFavourite(1, 0);

        verify(recipeRepository).getFavourite(1, PageRequest.of(0, 6));
    }

    @Test
    void getFavouriteForAccountThrowsWithInvalidPage() {

        assertThatThrownBy(() -> testRecipeService.getFavourite(1, -2))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid page");
    }

    @Test
    void getDolaterForAccountWorks() {
        given(recipeRepository.getDoLater(anyInt(), any())).willReturn(List.of(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));
        testRecipeService.getDoLater(1, 0);

        verify(recipeRepository).getDoLater(1, PageRequest.of(0, 6));
    }

    @Test
    void getDoLateForAccountThrowsWithInvalidPage() {

        assertThatThrownBy(() -> testRecipeService.getDoLater(1, -1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid page");
    }

    @Test
    void getRecipeFromDBWorks() {
        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        true,
                        LocalDate.of(2022, 12, 12),
                        "test instructions",
                        List.of(new Category()),
                        List.of(new Type()),
                        new Account(),
                        List.of(new Country(1, "test Country")),
                        List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12))))
                );
        FullRecipeRes res = testRecipeService.getRecipe(1);
        verify(recipeRepository).findById(1);

        assertEquals("test Country", res.getCuisines().get(0));
    }

    @Test
    void getRecipeFromDBThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> testRecipeService.getRecipe(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void getRecipeByDateForAccountWorks() {
        given(recipeRepository.getByDate(anyInt(), any()))
                .willReturn(Optional.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        true,
                        LocalDate.of(2022,12,12),
                        "test instructions",
                        List.of(new Category()),
                        List.of(new Type()),
                        new Account(),
                        List.of(new Country(1, "test Country")),
                        List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12))))
                );
        FullRecipeRes res = testRecipeService.getRecipeForDate(1, LocalDate.of(2022,12,12));

        verify(recipeRepository).getByDate(1, LocalDate.of(2022,12,12));
        assertEquals("test Country", res.getCuisines().get(0));
    }

    @Test
    void getRecipeByDateForAccountWorksWithNoRecipe() {
        given(recipeRepository.getByDate(anyInt(), any())).willReturn(Optional.empty());

        FullRecipeRes res = testRecipeService.getRecipeForDate(1, LocalDate.of(2022,12,12));

        verify(recipeRepository).getByDate(1, LocalDate.of(2022,12,12));
        assertNull(res);
    }

    @Test
    void getCalendarWorks() {
        given(recipeRepository.getByDate(anyInt(), any()))
                .willReturn(Optional.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        true,
                        LocalDate.of(2022,12,12),
                        "test instructions",
                        List.of(new Category()),
                        List.of(new Type()),
                        new Account(),
                        List.of(new Country(1, "test Country")),
                        List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12))))
                );
        Map<String, Day> res = testRecipeService.getCalendar(1);
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        assertEquals(monday, res.get("Monday").getDate());
        assertEquals(true, res.get("Monday").getIsRecipe());
        assertEquals(true, res.get("Monday").getIsFinished());
    }

    @Test
    void getCalendarWorksWithNotFinished() {
        given(recipeRepository.getByDate(anyInt(), any()))
                .willReturn(Optional.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        false,
                        LocalDate.of(2022,12,12),
                        "test instructions",
                        List.of(new Category()),
                        List.of(new Type()),
                        new Account(),
                        List.of(new Country(1, "test Country")),
                        List.of(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12))))
                );
        Map<String, Day> res = testRecipeService.getCalendar(1);
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        assertEquals(monday, res.get("Monday").getDate());
        assertEquals(true, res.get("Monday").getIsRecipe());
        assertEquals(false, res.get("Monday").getIsFinished());
    }

    @Test
    void getCalendarWorksWithNoRecipe() {
        given(recipeRepository.getByDate(anyInt(), any()))
                .willReturn(Optional.empty());
        Map<String, Day> res = testRecipeService.getCalendar(1);
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        assertEquals(monday, res.get("Monday").getDate());
        assertEquals(false, res.get("Monday").getIsRecipe());
        assertEquals(false, res.get("Monday").getIsFinished());
    }


    @Test
    void deleteRecipeWorks() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "test country"));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "test category"));
        List<Type> types = new ArrayList<>();
        types.add(new Type(1, "test type"));
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12));

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(new Recipe(
                    1,
                    "title",
                    "recipe desc",
                    "recipe original",
                    12,
                    12,
                    "recipe img",
                    200,
                    true,
                    true,
                    true,
                        LocalDate.of(2022,12,12),
                    "test instructions",
                    categories,
                    types,
                    new Account(),
                    countries,
                    measurements
                )));
        testRecipeService.delete(1);
        verify(recipeRepository).deleteById(1);
    }

    @Test
    void deleteRecipeThrowsWithNoMatchingRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testRecipeService.delete(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void deleteAccountThrowsErrorWithFailedDeletion() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "test country"));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "test category"));
        List<Type> types = new ArrayList<>();
        types.add(new Type(1, "test type"));
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(new Measurement(1, new Unit(1, "test"), new Ingredient(1, "test"), 12));

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(new Recipe(
                        1,
                        "title",
                        "recipe desc",
                        "recipe original",
                        12,
                        12,
                        "recipe img",
                        200,
                        true,
                        true,
                        true,
                        LocalDate.of(2022,12,12),
                        "test instructions",
                        categories,
                        types,
                        new Account(),
                        countries,
                        measurements
                )));
        doThrow(new RuntimeException("error")).when(recipeRepository).deleteById(any());
        assertThatThrownBy(() ->  testRecipeService.delete(0))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("error while deleting from database");

        verify(recipeRepository).deleteById(0);
    }

    @Test
    void randomRecipeWorks() {
        given(recipeUtils.randomResults())
                .willReturn(new RandomResponse());

        testRecipeService.getRandom();

        verify(recipeUtils).randomResults();
    }
}


