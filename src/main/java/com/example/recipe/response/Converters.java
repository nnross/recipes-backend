package com.example.recipe.response;

import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.recipe.Recipe;
import com.example.recipe.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for converters to convert entities to returnable versions
 */
public class Converters {
    /**
     * Converts recipe to full returnable recipe
     * @param recipe
     *        recipe to convert.
     * @return converted recipe
     */
    public FullRecipeRes fullRecipeConverter(Recipe recipe) {
        List<String> types = new ArrayList<>();
        List<MeasurementRes> measurements = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        List<String> countries = new ArrayList<>();

        for (Type type : recipe.getType()) {
            types.add(type.getName());
        }

        for (Country country : recipe.getCountry()) {
            countries.add(country.getName());
        }

        for (Measurement measurement : recipe.getMeasurements()) {
            measurements.add(new MeasurementRes(measurement.getIngredient(), measurement.getAmount(), measurement.getUnit()));
        }

        for (Category category : recipe.getCategory()) {
            categories.add(category.getName());
        }

        return new FullRecipeRes(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getImage(),
                recipe.getServings(),
                recipe.getTime(),
                recipe.getOriginal(),
                recipe.getInstructions(),
                recipe.getDescription(),
                recipe.getHealthScore(),
                recipe.getAccount().getId(),
                recipe.getFavourite(),
                recipe.getDoLater(),
                recipe.getFinished(),
                recipe.getToDoDate(),
                categories,
                countries,
                types,
                measurements
        );
    }
}
