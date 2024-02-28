package api.tests.demo;

import api.tests.demo.config.ConfigContainer;
import api.tests.demo.constants.Endpoints;
import api.tests.demo.constants.Groups;
import api.tests.demo.requests.ServiceRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static api.tests.demo.validations.CocktailRecipesValidations.*;
import static api.tests.demo.validations.CommonValidations.combine;
import static api.tests.demo.validations.CommonValidations.ok;

public class CocktailRecipesTests extends BaseTest {
    
    @Test(groups = { Groups.ALL, Groups.POSITIVE })
    public void getRecipesByCocktailNameTest() {
        String cocktailName = "margarita";

        Set<String> expectedIngredients = new HashSet<>();
        expectedIngredients.add("Tequila");
        expectedIngredients.add("Triple sec");
        expectedIngredients.add("Lime juice");
        expectedIngredients.add("Salt");

        callEndpoint(cocktailName, combine(
            ok(),
            resultSizeMustBeAtLeast(1),
            resultItemMatchesJsonSchema("recipeItem.json"),
            ingredientsMustContain(cocktailName, expectedIngredients)
        ));
    }

    @Test(groups = { Groups.ALL, Groups.NEGATIVE },
            description = "Fake cocktail name")
    public void getRecipeForFakeCocktailNameTest() {
        callEndpoint("Fake Cocktail Name", resultMustHaveSize(0));
    }

    @Step("Call endpoint")
    private void callEndpoint(String cocktailName, Consumer<Response> validator) {
        Response response = new ServiceRequest(
            ConfigContainer.getConfig().getCocktailRecipesServiceHost(),
            Endpoints.GET_COCKTAIL_RECIPE
        ).queryParams(Map.of("s", cocktailName))
        .GET();
        validator.accept(response);
    }
}
