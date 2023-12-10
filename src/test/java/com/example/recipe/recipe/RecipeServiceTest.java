package com.example.recipe.recipe;

import com.example.recipe.RecipeApplication;
import com.example.recipe.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = RecipeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private RecipeService testRecipeService;

    @Test
    void getRecipeFromDBWorks() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getRecipeFromDB(1);
        verify(recipeRepository).findById(1);
    }

    @Test
    void getRecipeFromAPIWorks() {
        // TODO: mock API
        testRecipeService.getRecipeFromAPI(1);
        //TOOD: verify API call
    }

    @Test
    void searchRecipeWorks() {
        //TODO: mock API
        testRecipeService.search("search", "sort", "sortDir", "filters");
        // TODO: verify API call
    }

    @Test
    void searchRecipeThrowsWithInvalidSearch() {
        //TODO: mock API
        assertThatThrownBy(() ->          testRecipeService.search("search", "sort", "sortDir", "filters"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid search");
    }
    @Test
    void searchRecipeThrowsWithInvalidSort() {
        //TODO: mock API
        assertThatThrownBy(() ->          testRecipeService.search("search", "sort", "sortDir", "filters"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort");
    }
    @Test
    void searchRecipeThrowsWithInvalidSortDir() {
        //TODO: mock API
        assertThatThrownBy(() ->          testRecipeService.search("search", "sort", "sortDir", "filters"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort direction");
    }
    @Test
    void searchRecipeThrowsWithInvalidFilters() {
        //TODO: mock API
        assertThatThrownBy(() ->          testRecipeService.search("search", "sort", "sortDir", "filters"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid filters");
    }

    @Test
    void getRecipeByDateForAccountWorks() {
        given(recipeRepository.findByDate(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getRecipeByDate(1, new Date(2022, 12, 12));

        verify(recipeRepository).findByDate(1, newDate(2022, 12, 12));
    }

    @Test
    void getRecipeByDateForAccountThrowsWithInvalidDate() {
        assertThatThrownBy(() ->  testRecipeService.getRecipeByDate(1, new Date(2022, 20, 12)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid Date");

        verify(recipeRepository, never()).findByDate(account);
    }

    @Test
    void getFavouriteForAccountWorks() {
        given(recipeRepository.findFavourite(any())).willReturn(Optional.of(new ArrayList<Recipe>(new Recipe())));
        testRecipeService.getFavourites(1, 0);

        verify(recipeRepository).findFavourites(1);
    }

    @Test
    void getCalendarWorks() {
        given(recipeRepository.findByDate(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.getCalenadr(1, new Date(2022, 12, 12));

        verify(recipeRepository).findByDate(1, newDate(2022, 12, 12));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 13));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 14));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 15));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 16));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 17));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 18));
        verify(recipeRepository).findByDate(1, newDate(2022, 12, 19));
    }

    @Test
    void getCalendarThrowsWithInvalidDate() {
        assertThatThrownBy(() ->  testRecipeService.getCalendar(1, new Date(2022, 20, 12)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid Date");

        verify(recipeRepository, never()).findByDate(account);
    }

    @Test
    void getDoLaterForAccountWorks() {
        given(recipeRepository.findDoLater(any())).willReturn(Optional.of(new ArrayList<Recipe>(new Recipe())));
        testRecipeService.getDoLater(1, 0);

        verify(recipeRepository).findDoLater(1);
    }

    @Test
    void getStatsForAccountWorks() {
        given(recipeRepository.findCountry(any())).willReturn(Optional.of(new ArrayList<Stats>(new Stats())));
        given(recipeRepository.findStats(any())).willReturn(Optional.of(new ArrayList<Stats>(new Stats())));
        testRecipeService.getFavourites(1);

        verify(recipeRepository).findStats(1);
        verify(recipeRepository).findCountry(1);
    }

    @Test
    void favouriteRecipeWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.favourite(true, 1);
        verify(recipeRepository).save(1);
    }

    @Test
    void doLaterRecipeWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.doLater(true, 1);
        verify(recipeRepository).save(1);
    }

    @Test
    void unFavouriteRecipeWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.favourite(false, 1);
        verify(recipeRepository).save(1);
    }

    @Test
    void unDoLaterRecipeWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.doLater(false, 1);
        verify(recipeRepository).save(1);
    }

    @Test
    void finishRecipeWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.favourite(1);
        verify(recipeRepository).save(1);
    }

    @Test
    void deleteRecipeWorks() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));
        testRecipeService.delete(1);
        verify(recipeRepository).delete(1);
    }

    @Test
    void deleteRecipeThrowsWithNoMatchingRecipe() {
        given(recipeRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testRecipeService.deleteRecipe(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No Recipes exists with id 0");

        verify(recipeRepository, never()).deleteById(0);
    }

    @Test
    void deleteAccountThrowsErrorWithFailedDeletion() {
        given(recipeRepository.findById(any())).willReturn(Optional.of(new Recipe()));

        assertThatThrownBy(() ->  testRecipeService.deleteRecipe(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("deletion failed");

        verify(recipeRepository).deleteById(0);
    }
}


