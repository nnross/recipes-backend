package com.example.recipe.recipe;

import com.example.recipe.RecipeApplication;
import com.example.recipe.account.Account;
import com.example.recipe.account.AccountRepository;
import com.example.recipe.security.JwtService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="secret.key=cHJvcGVydHlzdWJzdGFuY2V3aXRocmlkaW5nZ3JlYXRhcnRpY2xld2l0aGluZGlzYXA")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeIntegrationTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private WebTestClient webClient;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("requestUrl",
                () -> mockWebServer.url("/").toString());
    }

    @Test
    void getRecipeFromDBWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/db?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test title")
                .jsonPath("$.image").isEqualTo("test image")
                .jsonPath("$.servings").isEqualTo(2)
                .jsonPath("$.readyInMinutes").isEqualTo(12)
                .jsonPath("$.sourceUrl").isEqualTo("test original")
                .jsonPath("$.instructions").isEqualTo("test instruction")
                .jsonPath("$.healthScore").isEqualTo(120)
                .jsonPath("$.account").isEqualTo(1)
                .jsonPath("$.favourite").isEqualTo(false)
                .jsonPath("$.doLater").isEqualTo(false)
                .jsonPath("$.finished").isEqualTo(false)
                .jsonPath("$.date").isEqualTo("2022-12-12")
                .jsonPath("$.dishTypes[0]").isEqualTo("test category")
                .jsonPath("$.diets[0]").isEqualTo("test type")
                .jsonPath("$.cuisines[0]").isEqualTo("test country")
                .jsonPath("$.measurements[0].unit").isEqualTo("test unit")

                .jsonPath("$.id").isEqualTo("1");
    }

    @Test
    void getRecipeFromAPIWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(
                                """
                                {
                                    "vegetarian": false,
                                    "vegan": false,
                                    "glutenFree": true,
                                    "dairyFree": false,
                                    "veryHealthy": false,
                                    "cheap": false,
                                    "veryPopular": false,
                                    "sustainable": false,
                                    "lowFodmap": false,
                                    "weightWatcherSmartPoints": 13,
                                    "gaps": "no",
                                    "preparationMinutes": 10,
                                    "cookingMinutes": 30,
                                    "aggregateLikes": 0,
                                    "healthScore": 22,
                                    "creditsText": "Kraft Recipes",
                                    "sourceName": "Kraft Recipes",
                                    "pricePerServing": 314.01,
                                    "extendedIngredients": [
                                        {
                                            "id": 12117,
                                            "aisle": "Canned and Jarred",
                                            "image": "coconut-milk.png",
                                            "consistency": "LIQUID",
                                            "name": "coconut milk",
                                            "nameClean": "unsweetened coconut milk",
                                            "original": "1 can (13.5 oz) coconut milk",
                                            "originalName": "can coconut milk",
                                            "amount": 13.5,
                                            "unit": "oz",
                                            "meta": [
                                                "canned"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 13.5,
                                                    "unitShort": "oz",
                                                    "unitLong": "ounces"
                                                },
                                                "metric": {
                                                    "amount": 382.719,
                                                    "unitShort": "g",
                                                    "unitLong": "grams"
                                                }
                                            }
                                        },
                                        {
                                            "id": 1017,
                                            "aisle": "Cheese",
                                            "image": "cream-cheese.jpg",
                                            "consistency": "SOLID",
                                            "name": "philadelphia cream cheese",
                                            "nameClean": "cream cheese",
                                            "original": "2 oz. (1/4 of 8-oz. pkg.) PHILADELPHIA Cream Cheese, cubed",
                                            "originalName": "(1/4 of 8-oz. pkg.) PHILADELPHIA Cream Cheese, cubed",
                                            "amount": 2.0,
                                            "unit": "oz",
                                            "meta": [
                                                "cubed",
                                                "()"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 2.0,
                                                    "unitShort": "oz",
                                                    "unitLong": "ounces"
                                                },
                                                "metric": {
                                                    "amount": 56.699,
                                                    "unitShort": "g",
                                                    "unitLong": "grams"
                                                }
                                            }
                                        },
                                        {
                                            "id": 10011819,
                                            "aisle": "Ethnic Foods",
                                            "image": "scotch-bonnet-chile.jpg",
                                            "consistency": "SOLID",
                                            "name": "habanero chile",
                                            "nameClean": "habanero chili",
                                            "original": "1 habanero chile, seeded, deveined and thinly sliced",
                                            "originalName": "habanero chile, seeded, deveined and thinly sliced",
                                            "amount": 1.0,
                                            "unit": "",
                                            "meta": [
                                                "deveined",
                                                "seeded",
                                                "thinly sliced"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                },
                                                "metric": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                }
                                            }
                                        },
                                        {
                                            "id": 10011819,
                                            "aisle": "Ethnic Foods",
                                            "image": "habanero-pepper.jpg",
                                            "consistency": "SOLID",
                                            "name": "habanero chile",
                                            "nameClean": "habanero chili",
                                            "original": "1 habanero chile, seeded, deveined and thinly sliced",
                                            "originalName": "habanero chile, seeded, deveined and thinly sliced",
                                            "amount": 1.0,
                                            "unit": "",
                                            "meta": [
                                                "deveined",
                                                "seeded",
                                                "thinly sliced"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                },
                                                "metric": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                }
                                            }
                                        },
                                        {
                                            "id": 4114,
                                            "aisle": "Oil, Vinegar, Salad Dressing",
                                            "image": "italian-dressing.jpg",
                                            "consistency": "LIQUID",
                                            "name": "1/2 cup kraft zesty italian dressing",
                                            "nameClean": "salad dressing",
                                            "original": "1/2 cup KRAFT Zesty Italian Dressing",
                                            "originalName": "KRAFT Zesty Italian Dressing",
                                            "amount": 0.5,
                                            "unit": "cup",
                                            "meta": [
                                                "italian",
                                                "kraft"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 0.5,
                                                    "unitShort": "cups",
                                                    "unitLong": "cups"
                                                },
                                                "metric": {
                                                    "amount": 117.5,
                                                    "unitShort": "ml",
                                                    "unitLong": "milliliters"
                                                }
                                            }
                                        },
                                        {
                                            "id": 11282,
                                            "aisle": "Produce",
                                            "image": "brown-onion.png",
                                            "consistency": "SOLID",
                                            "name": "onion",
                                            "nameClean": "onion",
                                            "original": "1 onion, thinly sliced",
                                            "originalName": "onion, thinly sliced",
                                            "amount": 1.0,
                                            "unit": "",
                                            "meta": [
                                                "thinly sliced"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                },
                                                "metric": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                }
                                            }
                                        },
                                        {
                                            "id": 11821,
                                            "aisle": "Produce",
                                            "image": "red-pepper.jpg",
                                            "consistency": "SOLID",
                                            "name": "bell pepper",
                                            "nameClean": "red pepper",
                                            "original": "1 red pepper, chopped",
                                            "originalName": "red pepper, chopped",
                                            "amount": 1.0,
                                            "unit": "",
                                            "meta": [
                                                "red",
                                                "chopped"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                },
                                                "metric": {
                                                    "amount": 1.0,
                                                    "unitShort": "",
                                                    "unitLong": ""
                                                }
                                            }
                                        },
                                        {
                                            "id": 15101,
                                            "aisle": "Seafood",
                                            "image": "red-snapper.jpg",
                                            "consistency": "SOLID",
                                            "name": "snapper",
                                            "nameClean": "red snapper",
                                            "original": "1 whole red snapper (2 lb.), cleaned",
                                            "originalName": "whole red snapper , cleaned",
                                            "amount": 2.0,
                                            "unit": "lb",
                                            "meta": [
                                                "whole",
                                                "red",
                                                "cleaned"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 2.0,
                                                    "unitShort": "lb",
                                                    "unitLong": "pounds"
                                                },
                                                "metric": {
                                                    "amount": 907.185,
                                                    "unitShort": "g",
                                                    "unitLong": "grams"
                                                }
                                            }
                                        },
                                        {
                                            "id": 15101,
                                            "aisle": "Seafood",
                                            "image": "red-snapper-fresh.jpg",
                                            "consistency": "SOLID",
                                            "name": "snapper",
                                            "nameClean": "red snapper",
                                            "original": "1 whole red snapper (2 lb.), cleaned",
                                            "originalName": "whole red snapper , cleaned",
                                            "amount": 2.0,
                                            "unit": "lb",
                                            "meta": [
                                                "whole",
                                                "red",
                                                "cleaned"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 2.0,
                                                    "unitShort": "lb",
                                                    "unitLong": "pounds"
                                                },
                                                "metric": {
                                                    "amount": 907.185,
                                                    "unitShort": "g",
                                                    "unitLong": "grams"
                                                }
                                            }
                                        },
                                        {
                                            "id": 15101,
                                            "aisle": "Seafood",
                                            "image": "snapper.jpg",
                                            "consistency": "SOLID",
                                            "name": "snapper",
                                            "nameClean": "red snapper",
                                            "original": "1 whole red snapper (2 lb.), cleaned",
                                            "originalName": "whole red snapper , cleaned",
                                            "amount": 2.0,
                                            "unit": "lb",
                                            "meta": [
                                                "whole",
                                                "red",
                                                "cleaned"
                                            ],
                                            "measures": {
                                                "us": {
                                                    "amount": 2.0,
                                                    "unitShort": "lb",
                                                    "unitLong": "pounds"
                                                },
                                                "metric": {
                                                    "amount": 907.185,
                                                    "unitShort": "g",
                                                    "unitLong": "grams"
                                                }
                                            }
                                        }
                                    ],
                                    "id": 6253,
                                    "title": "Red Snapper in Coconut Sauce",
                                    "readyInMinutes": 40,
                                    "servings": 6,
                                    "sourceUrl": "http://www.kraftrecipes.com/recipes/red-snapper-in-coconut-92021.aspx",
                                    "image": "https://spoonacular.com/recipeImages/6253-556x370.jpg",
                                    "imageType": "jpg",
                                    "summary": "Red Snapper in Coconut Sauce might be a good recipe to expand your main course recipe box. Watching your figure? This gluten free, primal, pescatarian, and ketogenic recipe has <b>391 calories</b>, <b>34g of protein</b>, and <b>25g of fat</b> per serving. For <b>$3.14 per serving</b>, this recipe <b>covers 26%</b> of your daily requirements of vitamins and minerals. This recipe serves 6. 1 person has made this recipe and would make it again. From preparation to the plate, this recipe takes roughly <b>40 minutes</b>. Head to the store and pick up coconut milk, bell pepper, 1/2 cup kraft zesty italian dressing, and a few other things to make it today. It is brought to you by Kraft Recipes. Taking all factors into account, this recipe <b>earns a spoonacular score of 61%</b>, which is solid. If you like this recipe, take a look at these similar recipes: <a href=\\"https://spoonacular.com/recipes/egyptian-red-snapper-in-red-pepper-mint-sauce-88484\\">Egyptian Red Snapper in Red Pepper Mint Sauce</a>, <a href=\\"https://spoonacular.com/recipes/grilled-red-snapper-with-roasted-red-pepper-sauce-6273\\">Grilled Red Snapper with Roasted Red Pepper Sauce</a>, and <a href=\\"https://spoonacular.com/recipes/red-snapper-with-red-curry-carrot-sauce-for-two-88452\\">Red Snapper With Red Curry Carrot Sauce for Two</a>.",
                                    "cuisines": ["asian"],
                                    "dishTypes": [
                                        "lunch",
                                        "main course",
                                        "main dish",
                                        "dinner"
                                    ],
                                    "diets": [
                                        "gluten free",
                                        "primal",
                                        "pescatarian"
                                    ],
                                    "occasions": [],
                                    "winePairing": {
                                        "pairedWines": [
                                            "pinot noir",
                                            "pinot grigio",
                                            "gruener veltliner"
                                        ],
                                        "pairingText": "Pinot Noir, Pinot Grigio, and Gruener Veltliner are great choices for Red Snapper. Fish is as diverse as wine, so it's hard to pick wines that go with every fish. A crisp white wine, such as a pinot grigio or Grüner Veltliner, will suit any delicately flavored white fish. Meaty, strongly flavored fish such as salmon and tuna can even handle a light red wine, such as a pinot noir. One wine you could try is Stringtown Pinot Noir. It has 4.8 out of 5 stars and a bottle costs about 17 dollars.",
                                        "productMatches": [
                                            {
                                                "id": 433916,
                                                "title": "Stringtown Pinot Noir",
                                                "description": "With expressive cherry and red berry fruit on the nose and a soft, supple palate, rounded off by sweet, spicy and complex oak notes, this fruit-driven Pinot Noir features just the right balance of acidity and delicate tannins to finish clean yet vibrant.",
                                                "price": "$16.989999771118164",
                                                "imageUrl": "https://spoonacular.com/productImages/433916-312x231.jpg",
                                                "averageRating": 0.9599999785423279,
                                                "ratingCount": 13.0,
                                                "score": 0.9349999785423279,
                                                "link": "https://click.linksynergy.com/deeplink?id=*QCiIS6t4gA&mid=2025&murl=https%3A%2F%2Fwww.wine.com%2Fproduct%2Fstringtown-pinot-noir-2013%2F152066"
                                            }
                                        ]
                                    },
                                    "instructions": "Score both sides of fish with shallow cross-cuts.  Heat dressing in large skillet on medium heat.  Add fish; cook 6 to 8 min. on each side or until fish is lightly browned on both sides and flakes easily with fork.  Transfer fish to platter; cover to keep warm.                                            Add coconut milk, onions and chiles to skillet; cook on medium-low heat 10 min. or until coconut milk is reduced by half.  Stir in peppers and cream cheese; cook 3 min. or until cream cheese is melted and sauce is well blended, stirring frequently.                                            Serve fish topped with sauce.",
                                    "analyzedInstructions": [
                                        {
                                            "name": "",
                                            "steps": [
                                                {
                                                    "number": 1,
                                                    "step": "Score both sides of fish with shallow cross-cuts.",
                                                    "ingredients": [
                                                        {
                                                            "id": 10115261,
                                                            "name": "fish",
                                                            "localizedName": "fish",
                                                            "image": "fish-fillet.jpg"
                                                        }
                                                    ],
                                                    "equipment": []
                                                },
                                                {
                                                    "number": 2,
                                                    "step": "Heat dressing in large skillet on medium heat.",
                                                    "ingredients": [],
                                                    "equipment": [
                                                        {
                                                            "id": 404645,
                                                            "name": "frying pan",
                                                            "localizedName": "frying pan",
                                                            "image": "pan.png"
                                                        }
                                                    ]
                                                },
                                                {
                                                    "number": 3,
                                                    "step": "Add fish; cook 6 to 8 min. on each side or until fish is lightly browned on both sides and flakes easily with fork.",
                                                    "ingredients": [
                                                        {
                                                            "id": 10115261,
                                                            "name": "fish",
                                                            "localizedName": "fish",
                                                            "image": "fish-fillet.jpg"
                                                        }
                                                    ],
                                                    "equipment": [],
                                                    "length": {
                                                        "number": 6,
                                                        "unit": "minutes"
                                                    }
                                                },
                                                {
                                                    "number": 4,
                                                    "step": "Transfer fish to platter; cover to keep warm.",
                                                    "ingredients": [
                                                        {
                                                            "id": 10115261,
                                                            "name": "fish",
                                                            "localizedName": "fish",
                                                            "image": "fish-fillet.jpg"
                                                        }
                                                    ],
                                                    "equipment": []
                                                },
                                                {
                                                    "number": 5,
                                                    "step": "Add coconut milk, onions and chiles to skillet; cook on medium-low heat 10 min. or until coconut milk is reduced by half.  Stir in peppers and cream cheese; cook 3 min. or until cream cheese is melted and sauce is well blended, stirring frequently.",
                                                    "ingredients": [
                                                        {
                                                            "id": 12118,
                                                            "name": "coconut milk",
                                                            "localizedName": "coconut milk",
                                                            "image": "coconut-milk.png"
                                                        },
                                                        {
                                                            "id": 1017,
                                                            "name": "cream cheese",
                                                            "localizedName": "cream cheese",
                                                            "image": "cream-cheese.jpg"
                                                        },
                                                        {
                                                            "id": 10111333,
                                                            "name": "peppers",
                                                            "localizedName": "peppers",
                                                            "image": "green-pepper.jpg"
                                                        },
                                                        {
                                                            "id": 11819,
                                                            "name": "chili pepper",
                                                            "localizedName": "chili pepper",
                                                            "image": "red-chili.jpg"
                                                        },
                                                        {
                                                            "id": 11282,
                                                            "name": "onion",
                                                            "localizedName": "onion",
                                                            "image": "brown-onion.png"
                                                        },
                                                        {
                                                            "id": 0,
                                                            "name": "sauce",
                                                            "localizedName": "sauce",
                                                            "image": ""
                                                        }
                                                    ],
                                                    "equipment": [
                                                        {
                                                            "id": 404645,
                                                            "name": "frying pan",
                                                            "localizedName": "frying pan",
                                                            "image": "pan.png"
                                                        }
                                                    ],
                                                    "length": {
                                                        "number": 13,
                                                        "unit": "minutes"
                                                    }
                                                },
                                                {
                                                    "number": 6,
                                                    "step": "Serve fish topped with sauce.",
                                                    "ingredients": [
                                                        {
                                                            "id": 0,
                                                            "name": "sauce",
                                                            "localizedName": "sauce",
                                                            "image": ""
                                                        },
                                                        {
                                                            "id": 10115261,
                                                            "name": "fish",
                                                            "localizedName": "fish",
                                                            "image": "fish-fillet.jpg"
                                                        }
                                                    ],
                                                    "equipment": []
                                                }
                                            ]
                                        }
                                    ],
                                    "originalId": null,
                                    "spoonacularScore": 64.97270202636719
                                }
                                """
                        )
        );

        webClient.get().uri("/api/recipe/get/api/id?id=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Red Snapper in Coconut Sauce")
                .jsonPath("$.image").isEqualTo("https://spoonacular.com/recipeImages/6253-556x370.jpg")
                .jsonPath("$.servings").isEqualTo(6)
                .jsonPath("$.readyInMinutes").isEqualTo(40)
                .jsonPath("$.sourceUrl").isEqualTo("http://www.kraftrecipes.com/recipes/red-snapper-in-coconut-92021.aspx")
                .jsonPath("$.instructions").isEqualTo("Score both sides of fish with shallow cross-cuts.  Heat dressing in large skillet on medium heat.  Add fish; cook 6 to 8 min. on each side or until fish is lightly browned on both sides and flakes easily with fork.  Transfer fish to platter; cover to keep warm.                                            Add coconut milk, onions and chiles to skillet; cook on medium-low heat 10 min. or until coconut milk is reduced by half.  Stir in peppers and cream cheese; cook 3 min. or until cream cheese is melted and sauce is well blended, stirring frequently.                                            Serve fish topped with sauce.")
                .jsonPath("$.healthScore").isEqualTo(22)
                .jsonPath("$.dishTypes[0]").isEqualTo("lunch")
                .jsonPath("$.diets[0]").isEqualTo("gluten free")
                .jsonPath("$.cuisines[0]").isEqualTo("asian")
                .jsonPath("$.measurements[0].unit").isEqualTo("g");
    }
    @Test
    void searchRecipeWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(
                                """
                                {
                                    "results": [
                                        {
                                            "id": 654959,
                                            "title": "Pasta With Tuna",
                                            "image": "https://spoonacular.com/recipeImages/654959-312x231.jpg",
                                            "imageType": "jpg"
                                        },
                                        {
                                            "id": 511728,
                                            "title": "Pasta Margherita",
                                            "image": "https://spoonacular.com/recipeImages/511728-312x231.jpg",
                                            "imageType": "jpg"
                                        },
                                        {
                                            "id": 654857,
                                            "title": "Pasta On The Border",
                                            "image": "https://spoonacular.com/recipeImages/654857-312x231.jpg",
                                            "imageType": "jpg"
                                        }
                                    ]
                                }
                                """
                        )
        );

        webClient.get().uri("/api/recipe/get/api/search?search=pasta&ingredients=&cuisine=&diet=&intolerances=&type=&sort=&sortDirection=&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipes[0].title").isEqualTo("Pasta With Tuna")
                .jsonPath("$.recipes[0].id").isEqualTo(654959)
                .jsonPath("$.recipes[1].title").isEqualTo("Pasta Margherita")
                .jsonPath("$.recipes[1].id").isEqualTo(511728)
                .jsonPath("$.recipes[2].title").isEqualTo("Pasta On The Border")
                .jsonPath("$.recipes[2].id").isEqualTo(654857);
    }

    @Test
    void getFavouriteWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/favourite?accountId=1&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipes[0].title").isEqualTo("test title 2")
                .jsonPath("$.recipes[0].id").isEqualTo(2);
    }
    @Test
    void getDoLaterWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/doLater?accountId=1&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recipes[0].title").isEqualTo("test title 2")
                .jsonPath("$.recipes[0].id").isEqualTo(2);
    }
    @Test
    void getRecipeByDateWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/recipe/get/date?accountId=1&date=2022-12-12")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test title")
                .jsonPath("$.image").isEqualTo("test image")
                .jsonPath("$.servings").isEqualTo(2)
                .jsonPath("$.readyInMinutes").isEqualTo(12)
                .jsonPath("$.sourceUrl").isEqualTo("test original")
                .jsonPath("$.instructions").isEqualTo("test instruction")
                .jsonPath("$.healthScore").isEqualTo(120)
                .jsonPath("$.account").isEqualTo(1)
                .jsonPath("$.favourite").isEqualTo(false)
                .jsonPath("$.doLater").isEqualTo(false)
                .jsonPath("$.finished").isEqualTo(false)
                .jsonPath("$.date").isEqualTo("2022-12-12")
                .jsonPath("$.dishTypes[0]").isEqualTo("test category")
                .jsonPath("$.diets[0]").isEqualTo("test type")
                .jsonPath("$.cuisines[0]").isEqualTo("test country")
                .jsonPath("$.measurements[0].unit").isEqualTo("test unit")
                .jsonPath("$.id").isEqualTo("1");
    }

    @Test
    void favouriteRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(false, recipe.getFavourite().booleanValue());
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/favourite?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(true, recipe.getFavourite().booleanValue());

    }
    @Test
    void doLaterRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(false, recipe.getDoLater().booleanValue());
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/doLater?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(true, recipe.getDoLater().booleanValue());
    }
    @Test
    void finishRecipeWorks() {
        Recipe recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFinished().booleanValue(), false);
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/recipe/finished?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        recipe = recipeRepository.findById(1).orElse(null);
        assertEquals(recipe.getFinished().booleanValue(), true);
    }
    @Test
    void deleteRecipeWorks() {
        int res = recipeRepository.findAll().size();
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        webClient.delete().uri("/api/recipe/del?recipeId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

        assertEquals(res - 1, recipeRepository.findAll().size());
    }
    @Test
    void addRecipeWorks() {
        Account account = new Account(
                1,
                "test username",
                "test name",
                "test email",
                "test"
        );
        String token = jwtService.newToken(account);
        int res = recipeRepository.findAll().size();
        webClient.post().uri("/api/recipe/add")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(
                            """
                            {
                                "title": "test title 3",
                                "description": "test desc",
                                "original": "test original",
                                "time": 2,
                                "servings": 4,
                                "image": "test src",
                                "favourite": false,
                                "doLater": false,
                                "finished": true,
                                "toDoDate": null,
                                "instructions": "test instructions",
                                "healthScore": 2,
                                "category": [{"id": 1}],
                                "type": [{"id": 1}],
                                "country": [{"id":1}],
                                "account": {"id": 1},
                                "measurements":
                                    [
                                        {
                                            "unit": {"id": 1},
                                            "ingredient": {"id": 1},
                                            "amount": 12
                                        }
                                    ]
                            }
                            """
                )
                .exchange()
                .expectStatus().isOk();

        assertEquals(recipeRepository.findAll().size(), res + 1);
    }
}
