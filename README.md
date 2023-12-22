# Recipes-backend
Backend for the recipes application.

[frontend](https://github.com/nnross/recipes-frontend)

## About
This is the backend for the recipes project and it's a Spring Boot application written in Java with Maven.
It receives calls from the frontend, responds to them and communicates with the API and database, which is a MySQL database hosted in AWS.
As the API for the recipes we are using [Spoonacular](https://spoonacular.com/food-api).

It has unit tests that cover 100% of the methods and integration tests for all the API endpoints. 

## Prerequisites
To run and test you will need Java and Maven installed.

## Configuration
This project has the application.properties file that is not included in GitHub as it holds sensitive information.
To get the file you can contact iiro.s.partanen@gmail.com

When you have the file just add it to the main/resources where the application-test.properties are located.

The tests can be run without this file.

## Running
### Build
To run this application you will need to fulfill configuration and prerequisites.

To run the application yourself run `mvn clean install` and `mvn spring-boot:run` and the application should start.
If you want to run from the IDE run first the `mvn clean install` and then run the root class RecipeApplication in your IDE.
### Test
To run the tests you just need to clone the repository and from the root directory in the terminal run `mvn test`.
If you would like to run specific tests you can run them with the command `mvn test -Dtest=”TestClassName#TestMethodName”`.
You can also run the tests from your IDE by running the classes/methods.

## Technologies
This backend is done using Java with Maven and Spring Boot. Java was chosen instead of Node, which we were already familiar with, to learn Spring Boot.

The Database is a MySQL database and it is hosted in AWS. MySQL is one of the most popular options and having skills with AWS seemed useful,
so that's why it was chosen for this project.

Testing is done with JUnit and Spring MVC Test framework.
