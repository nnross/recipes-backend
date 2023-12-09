package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.type.Type;
import jakarta.persistence.*;

import java.sql.Date;

/**
 * Creates the recipe entity for the database.
 */
@Entity
@Table(name="recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "recipe_title", nullable = false)
    private String title;

    @Column(name = "recipe_description", nullable = false)
    private String description;

    @Column(name = "recipe_time", nullable = false)
    private int time;

    @Column(name = "recipe_servings", nullable = false)
    private int servings;

    @Column(name = "recipe_image", nullable = false)
    private String image;

    @Column(name = "recipe_favourite", nullable = false)
    private Boolean favourite;

    @Column(name = "recipe_doLater", nullable = false)
    private Boolean doLater;

    @Column(name = "recipe_finished", nullable = false)
    private Boolean finished;

    @Column(name = "recipe_toDoDate", nullable = false)
    private Date toDoDate;

    @Column(name = "recipe_instructions", nullable = false)
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "recipe_category", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "recipe_type", referencedColumnName = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "recipe_account", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "recipe_country", referencedColumnName = "country_id", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "recipe_measurements", referencedColumnName = "measurement_id", nullable = false)
    private Measurement measurements;

    public Recipe(int id, String title, String description, int time, int servings, String image, Boolean favourite, Boolean doLater, Boolean finished, Date toDoDate, String instructions, Category category, Type type, Account account, Country country, Measurement measurements) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.servings = servings;
        this.image = image;
        this.favourite = favourite;
        this.doLater = doLater;
        this.finished = finished;
        this.toDoDate = toDoDate;
        this.instructions = instructions;
        this.category = category;
        this.type = type;
        this.account = account;
        this.country = country;
        this.measurements = measurements;
    }

    public Recipe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Boolean getDoLater() {
        return doLater;
    }

    public void setDoLater(Boolean doLater) {
        this.doLater = doLater;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Date getToDoDate() {
        return toDoDate;
    }

    public void setToDoDate(Date toDoDate) {
        this.toDoDate = toDoDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Measurement getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Measurement measurements) {
        this.measurements = measurements;
    }
}
