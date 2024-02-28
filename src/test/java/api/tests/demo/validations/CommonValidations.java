package api.tests.demo.validations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.function.Consumer;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class CommonValidations {

    public static Consumer<Response> matchesJsonSchema(String schema) {
        return response -> {
            File jsonSchemaFile = new File("src/test/resources/schemes/" + schema)
                    .getAbsoluteFile();
            assertThat(
                "Json schema mismatch",
                response.asString(),
                JsonSchemaValidator.matchesJsonSchema(jsonSchemaFile)
            );
        };
    }
    
    public static Consumer<Response> ok() {
        return responseStatusMustBe(200);
    }

    public static Consumer<Response> responseStatusMustBe(int expected) {
        return response -> assertEquals(
            response.getStatusCode(),
            expected,
            "Unexpected response status code"
        );
    }

    public static Consumer<Response> clientError() {
        return response -> assertTrue(
            response.getStatusCode() >= 400 && response.getStatusCode() < 500,
            "Expected status code to be 4xx, actual: %s".formatted(response.getStatusCode())
        );
    }

    @SafeVarargs
    public static Consumer<Response> combine(Consumer<Response>... consumers) {
        return response -> {
            for (Consumer<Response> consumer : consumers) {
                consumer.accept(response);
            }
        };
    }
}
