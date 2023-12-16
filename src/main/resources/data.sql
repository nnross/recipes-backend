INSERT INTO account (account_name, account_username, account_email, account_password) VALUES ('test name', 'test username', 'test email', '$2a$10$0ooeofhWH.dzRKtwBUI6l.mcPaj7TZAoBcNpHUEFmeQhY0SPYecd6');

INSERT INTO type (type_name) VALUE ('test type');
INSERT INTO country (country_name) VALUE ('test country');
INSERT INTO category (category_name) VALUE ('test category');
INSERT INTO ingredient (ingredient_name) VALUE ('test ingredient');
INSERT INTO unit (unit_name) VALUE ('test unit');

INSERT INTO recipe (recipe_title, recipe_description, recipe_do_later, recipe_favourite, recipe_finished, recipe_health_score, recipe_image, recipe_instructions, recipe_original, recipe_servings, recipe_time, recipe_to_do_date, recipe_account) VALUES ('test title', 'test desc', false, false, false, 120, "test image", "test instructions", "test original", 2, 12, null, 1);
INSERT INTO recipe_category_mapping (recipe_category, category_id) VALUES (1, 1);
INSERT INTO recipe_type_mapping (recipe_type, type_id) VALUES (1, 1);
INSERT INTO recipe_country_mapping (recipe_country, country_id) VALUES (1, 1);
INSERT INTO measurement (measurement_amount, measurement_ingredient, measurement_unit, recipe_id) VALUES (12, 1, 1, 1);