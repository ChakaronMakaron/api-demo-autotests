package api.tests.demo.validations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class CocktailRecipesValidations {
    
    public static Consumer<Response> ingredientsMustContain(
        String drinkName,
        Set<String> ingredients
    ) {
        return response -> {
            JsonNode recipe = findRecipeWithName(response, drinkName);

            String field;
            Iterator<String> iterator = recipe.fieldNames();
            while (iterator.hasNext()) {
                field = iterator.next();
                if (!field.startsWith("strIngredient")) {
                    continue;
                }
                String fieldValue = recipe.path(field).asText();
                ingredients.remove(fieldValue);
                ingredients.remove(fieldValue.toLowerCase());
            }

            assertTrue(ingredients.isEmpty(),
                    "Could not find ingredients: %s".formatted(ingredients));
        };
    }

    public static Consumer<Response> resultSizeMustBeAtLeast(int expected) {
        return response -> {
            int actualSize = response.jsonPath().getInt("drinks.size()");
            assertTrue(
                actualSize >= expected,
                "Expected at least %s items, actual: %s".formatted(expected, actualSize)
            );
        };
    }

    public static Consumer<Response> resultMustHaveSize(int expected) {
        return response -> {
            assertEquals(
                response.jsonPath().getInt("drinks.size()"),
                expected,
                "Unexpected items size"
            );
        };
    }

    public static Consumer<Response> resultItemMatchesJsonSchema(String schema) {
        return response -> {
            File jsonSchemaFile = new File("src/test/resources/schemes/" + schema)
                    .getAbsoluteFile();
            JsonNode responseJsonNode = response.as(JsonNode.class);
            assertThat(
                "Drink item does not match json schema",
                responseJsonNode.at("/drinks/0").toString(),
                JsonSchemaValidator.matchesJsonSchema(jsonSchemaFile)
            );
        };
    }

    private static JsonNode findRecipeWithName(Response response, String name) {
        JsonNode responseJsonNode = response.as(JsonNode.class);
            Iterator<JsonNode> iterator = responseJsonNode.at("/drinks")
                .iterator();

            Optional<JsonNode> item = Optional.empty();

            JsonNode current;
            while (iterator.hasNext()) {
                current = iterator.next();
                if (current.at("/strDrink").asText().equalsIgnoreCase(name)) {
                    item = Optional.of(current);
                    break;
                }
            }

            if (item.isEmpty()) {
                fail("No recipe with 'strDrink' == '%s'".formatted(name));
            }

            return item.get();
    }
}
