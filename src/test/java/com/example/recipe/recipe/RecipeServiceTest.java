package com.example.recipe.recipe;

import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.apiClasses.Measures;
import com.example.recipe.apiClasses.Metric;
import com.example.recipe.apiClasses.RecipeFormat;
import com.example.recipe.apiClasses.RecipeIngredients;
import com.example.recipe.category.Category;
import com.example.recipe.category.CategoryRepository;
import com.example.recipe.country.Country;
import com.example.recipe.country.CountryRepository;
import com.example.recipe.ingredient.Ingredient;
import com.example.recipe.ingredient.IngredientRepository;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.measurement.MeasurementRepository;
import com.example.recipe.response.RecipeRes;
import com.example.recipe.response.StatRes;
import com.example.recipe.security.JwtService;
import com.example.recipe.type.Type;
import com.example.recipe.type.TypeRepository;
import com.example.recipe.unit.Unit;
import com.example.recipe.unit.UnitRepository;
import exceptions.BadRequestException;
import org.junit.jupiter.api.Disabled;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = {RecipeApplication.class, Intolerance.class, Types.class, Diet.class, Cuisine.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
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
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );
        testRecipeService.add(recipe);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void addRecipeThrowsWithBadUnit() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(unitRepository.findById(any())).willThrow(new RuntimeException("error"));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("unit not in database");
    }

    @Test
    void addRecipeThrowsWithIngredient() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(ingredientRepository.findById(any())).willThrow(new RuntimeException("error"));
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("ingredient not in database");
    }

    @Test
    void addRecipeThrowsWithBadType() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(typeRepository.findById(any())).willThrow(new RuntimeException("error"));
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("type not in database");
    }

    @Test
    void addRecipeThrowsWithBadCountry() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(countryRepository.findById(any())).willThrow(new RuntimeException("error"));
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("country not in database");
    }

    @Test
    void addRecipeThrowsWithBadCategory() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(categoryRepository.findById(any())).willThrow(new RuntimeException("error"));
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("category not in database");
    }

    @Test
    void addRecipeThrowsWithErrorWhileSaving() {
        given(recipeRepository.save(any())).willReturn(Optional.of(new Recipe()));
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        given(unitRepository.findById(any())).willReturn(Optional.of(new Unit()));
        given(typeRepository.findById(any())).willReturn(Optional.of(new Type()));
        given(countryRepository.findById(any())).willReturn(Optional.of(new Country()));
        given(ingredientRepository.findById(any())).willReturn(Optional.of(new Ingredient()));
        doThrow(new RuntimeException("error")).when(recipeRepository).save(any());
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
                Arrays.asList(new Category()),
                Arrays.asList(new Type()),
                new Account(),
                Arrays.asList(new Country()),
                Arrays.asList(new Measurement())
        );

        assertThatThrownBy(() -> testRecipeService.add(recipe))
                .isInstanceOf(BadRequestException.class)
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

        testRecipeService.toggleFavourite( 1);
        recipe.setFavourite(true);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void toggleFavouriteThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willThrow(new RuntimeException("error"));

        assertThatThrownBy(() -> testRecipeService.toggleFavourite(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void toggleFavouriteThrowsWithErrorWhileSaving() {
        Recipe recipe = new Recipe();
        recipe.setFavourite(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));
        doThrow(new RuntimeException("error")).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.toggleFavourite(1))
                .isInstanceOf(BadRequestException.class)
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

        testRecipeService.toggleDoLater( 1);
        recipe.setDoLater(true);
        verify(recipeRepository).save(recipe);

    }
    @Test
    void toggleDoLaterThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willThrow(new RuntimeException("error"));

        assertThatThrownBy(() -> testRecipeService.toggleDoLater(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void toggleDoLaterThrowsWithErrorWhileSaving() {
        Recipe recipe = new Recipe();
        recipe.setDoLater(true);
        given(recipeRepository.findById(any())).willReturn(Optional.of(recipe));
        doThrow(new RuntimeException("error")).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.toggleDoLater(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void finishRecipeWorks() {
        Recipe recipe = new Recipe();
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));

        testRecipeService.finishRecipe(1);
        recipe.setFinished(true);

        verify(recipeRepository).save(recipe);
    }

    @Test
    void finishRecipeThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willThrow(new RuntimeException("error"));

        assertThatThrownBy(() -> testRecipeService.finishRecipe(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void finishRecipeThrowsWithErrorWhileSaving() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        doThrow(new RuntimeException("error")).when(recipeRepository).save(any());

        assertThatThrownBy(() -> testRecipeService.finishRecipe(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while saving to database");
    }

    @Test
    void searchRecipeWorks() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());

        testRecipeService.getSearch("search", "pork", "asian", "vegan", "dairy", "dinner", "time", "asc", 0);

        verify(recipeUtils).searchResults("search", "pork", "asian", "vegan", "dairy", "dinner", "time", "asc", 0);
    }

    @Test
    void searchRecipeThrowsWithBadIngredient() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "bad", "asian", "vegan", "dairy", "dinner", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid ingredient filter");
    }

    @Test
    void searchRecipeThrowsWithBadCuisine() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "bad", "vegan", "dairy", "dinner", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid cuisine filter");
    }

    @Test
    void searchRecipeThrowsWithBadDiet() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "asian", "bad", "dairy", "dinner", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid diet filter");
    }

    @Test
    void searchRecipeThrowsWithBadIntolerance() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "asian", "vegan", "bad", "dinner", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid intolerance filter");
    }

    @Test
    void searchRecipeThrowsWithBadType() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "asian", "vegan", "dairy", "bad", "time", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid type filter");
    }

    @Test
    void searchRecipeThrowsWithBadSort() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "asian", "vegan", "dairy", "dinner", "bad", "asc", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid sort");
    }

    @Test
    void searchRecipeThrowsWithBadSortDir() {
        given(recipeUtils.searchResults(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(new RecipeResponse());


        assertThatThrownBy(() -> testRecipeService.getSearch("search", "pork", "asian", "vegan", "dairy", "dinner", "time", "bad", 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid sort direction");
    }

    @Test
    void getRecipeFromAPIWorks() {
        given(recipeUtils.getRecipeById(any()))
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
                        Arrays.asList("dinner"),
                        Arrays.asList("indian"),
                        Arrays.asList("vegan"),
                        Arrays.asList(new RecipeIngredients("test name", new Measures(new Metric(2, "tbsp"))))
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

        given(recipeRepository.getStats(any())).willReturn(list);
        given(recipeRepository.getDoneCount(any())).willReturn(Optional.of(12));
        given(recipeRepository.getFavouriteCount(any())).willReturn(Optional.of(2));
        given(recipeRepository.getDoLaterCount(any())).willReturn(Optional.of(4));

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

        given(recipeRepository.getStats(any())).willReturn(list);
        given(recipeRepository.getDoneCount(any())).willThrow(new RuntimeException("error"));
        given(recipeRepository.getFavouriteCount(any())).willReturn(Optional.of(2));
        given(recipeRepository.getDoLaterCount(any())).willReturn(Optional.of(4));

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

        given(recipeRepository.getStats(any())).willReturn(list);
        given(recipeRepository.getFavouriteCount(any())).willThrow(new RuntimeException("error"));
        given(recipeRepository.getDoneCount(any())).willReturn(Optional.of(2));
        given(recipeRepository.getDoLaterCount(any())).willReturn(Optional.of(4));

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

        given(recipeRepository.getStats(any())).willReturn(list);
        given(recipeRepository.getDoLaterCount(any())).willThrow(new RuntimeException("error"));
        given(recipeRepository.getDoneCount(any())).willReturn(Optional.of(2));
        given(recipeRepository.getFavouriteCount(any())).willReturn(Optional.of(4));

        assertThatThrownBy(() -> testRecipeService.getStats(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while getting doLater count from database");
    }

    @Test
    void getFavouriteForAccountWorks() {
        given(recipeRepository.getFavourite(any(), any())).willReturn(Arrays.asList(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));
        testRecipeService.getFavourite(1, -2);

        verify(recipeRepository).getFavourite(1, PageRequest.of(6, 0));
    }

    @Test
    void getFavouriteForAccountThrowsWithInvalidPage() {
        given(recipeRepository.getFavourite(any(), any())).willReturn(Arrays.asList(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));

        assertThatThrownBy(() -> testRecipeService.getFavourite(1, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid page");
    }

    @Test
    void getDolaterForAccountWorks() {
        given(recipeRepository.getDoLater(any(), any())).willReturn(Arrays.asList(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));
        testRecipeService.getDoLater(1, -2);

        verify(recipeRepository).getDoLater(1, PageRequest.of(6, 0));
    }

    @Test
    void getDoLateForAccountThrowsWithInvalidPage() {
        given(recipeRepository.getDoLater(any(), any())).willReturn(Arrays.asList(new ListRecipeRes() {
            @Override
            public String getTitle() {
                return "test";
            }

            @Override
            public int getId() {
                return 1;
            }
        }));

        assertThatThrownBy(() -> testRecipeService.getDoLater(1, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid page");
    }

    @Test
    void getRecipeFromDBWorks() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getRecipe(1);
        verify(recipeRepository).findById(1);
    }

    @Test
    void getRecipeFromDBThrowsWithNoRecipe() {
        given(recipeRepository.findById(any())).willThrow(new RuntimeException("error"));
        assertThatThrownBy(() -> testRecipeService.getRecipe(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid page");
    }

    @Test
    void getRecipeByDateForAccountWorks() {
        given(recipeRepository.getByDate(any(), any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getRecipeForDate(1, new Date(2022, 12, 12));

        verify(recipeRepository).getByDate(1, new Date(2022, 12, 12));
    }

    @Test
    void getRecipeByDateForAccountThrowsWithNoRecipe() {
        given(recipeRepository.getByDate(any(), any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getRecipeForDate(1, new Date(2022, 12, 12));

        assertThatThrownBy(() -> testRecipeService.getRecipeForDate(1, new Date(2022, 12, 12)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid page");
    }

    // TODO:
//    @Test
//    void getCalendarWorks() {
//        given(recipeRepository.findByDate(any())).willReturn(Optional.of(new Recipe()));
//        testRecipeService.getCalenadr(1, new Date(2022, 12, 12));
//
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 12));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 13));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 14));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 15));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 16));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 17));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 18));
//        verify(recipeRepository).findByDate(1, newDate(2022, 12, 19));
//    }

    //TODO:
//    @Test
//    void getCalendarThrowsWithInvalidDate() {
//        assertThatThrownBy(() ->  testRecipeService.getCalendar(1, new Date(2022, 20, 12)))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Invalid Date");
//
//        verify(recipeRepository, never()).findByDate(account);
//    }

    @Test
    void deleteRecipeWorks() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.delete(1);
        verify(recipeRepository).deleteById(1);
    }

    @Test
    void deleteRecipeThrowsWithNoMatchingRecipe() {
        given(recipeRepository.findById(any())).willThrow(new RuntimeException("error"));

        assertThatThrownBy(() ->  testRecipeService.delete(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no recipe with id");
    }

    @Test
    void deleteAccountThrowsErrorWithFailedDeletion() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        doThrow(new RuntimeException("error")).when(recipeRepository).deleteById(any());
        assertThatThrownBy(() ->  testRecipeService.delete(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("error while deleting from database");

        verify(recipeRepository).deleteById(0);
    }
}


