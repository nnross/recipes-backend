package com.example.recipe.recipe;

import com.example.recipe.account.Account;
import com.example.recipe.category.Category;
import com.example.recipe.country.Country;
import com.example.recipe.instructions.Instruction;
import com.example.recipe.measurement.Measurement;
import com.example.recipe.type.Type;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

/**
 * Creates the recipe entity for the database.
 */
@SuppressWarnings("unused")
@Entity
@Table(name="recipe")
public class Recipe {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "recipe_title", nullable = false)
    private String title;

    @Column(name = "recipe_description", nullable = false)
    private String description;

    @Column(name = "recipe_original")
    private String original;

    @Column(name = "recipe_time", nullable = false)
    private int time;

    @Column(name = "recipe_servings", nullable = false)
    private int servings;

    @Column(name = "recipe_image", nullable = false)
    private String image;

    @Column(name = "recipe_healthScore")
    private double healthScore;

    @Column(name = "recipe_favourite", nullable = false)
    private Boolean favourite;

    @Column(name = "recipe_doLater", nullable = false)
    private Boolean doLater;

    @Column(name = "recipe_finished", nullable = false)
    private Boolean finished;

    @Column(name = "recipe_toDoDate")
    private LocalDate toDoDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "recipe_instructions", nullable = false)
    private List<Instruction> instructions;

    @ManyToMany(cascade={CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(
            name = "recipe_category_mapping",
            joinColumns = @JoinColumn(name = "recipe_category"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;

    @ManyToMany(cascade={CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(
            name = "recipe_type_mapping",
            joinColumns = @JoinColumn(name = "recipe_type"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<Type> type;

    @ManyToOne
    @JoinColumn(name = "recipe_account", referencedColumnName = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    @ManyToMany(cascade={CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(
            name = "recipe_country_mapping",
            joinColumns = @JoinColumn(name = "recipe_country"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> country;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Measurement> measurements;

    public Recipe(int id, String title, String description, String original, int time, int servings, String image, double healthScore, Boolean favourite, Boolean doLater, Boolean finished, LocalDate toDoDate, List<Instruction> instructions, List<Category> category, List<Type> type, Account account, List<Country> country, List<Measurement> measurements) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.original = original;
        this.time = time;
        this.servings = servings;
        this.image = image;
        this.healthScore = healthScore;
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

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
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

    public LocalDate getToDoDate() {
        return toDoDate;
    }

    public void setToDoDate(LocalDate toDoDate) {
        this.toDoDate = toDoDate;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
