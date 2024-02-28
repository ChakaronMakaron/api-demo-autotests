package api.tests.demo.validations;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import io.restassured.response.Response;

public class EvenOrOddValidations {

    public static Consumer<Response> errorMustContain(String subStr) {
        return response -> {
            String errorMessage = response.jsonPath().getString("error");
            assertTrue(
                StringUtils.containsIgnoreCase(errorMessage, subStr),
                "'error' must contain '%s', actual: '%s'".formatted(subStr, errorMessage)
            );
        };
    }

    public static Consumer<Response> mustBeEven() {
        return response -> assertTrue(
            response.jsonPath().getBoolean("iseven"),
            "Expected even response"
        );
    }

    public static Consumer<Response> mustBeOdd() {
        return response -> assertFalse(
            response.jsonPath().getBoolean("iseven"),
            "Expected odd response"
        );
    }
}
