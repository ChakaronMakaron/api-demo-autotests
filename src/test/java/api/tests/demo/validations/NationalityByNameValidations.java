package api.tests.demo.validations;

import static org.testng.Assert.assertTrue;

import java.util.function.Consumer;

import io.restassured.response.Response;

public class NationalityByNameValidations {
    
    public static Consumer<Response> countriesSizeMustBeAtLeast(int expectedSize) {
        return response -> {
            int actualSize = response.jsonPath().getInt("country.size()");
            assertTrue(
                actualSize >= expectedSize,
                "Expected at least %s countries, actual: %s".formatted(
                    expectedSize, actualSize
                )
            );
        };
    }

    public static Consumer<Response> countryProbabilityMustBeAtLeast(
        String countryId,
        double expected
    ) {
        return response -> {
            double countryProbability = response.jsonPath()
                    .getDouble("country.find {it.country_id == '%s'}.probability"
                            .formatted(countryId));
            assertTrue(countryProbability >= expected,
                    "Unexpected probability for '%s', expected at least '%s', actual: '%s'".formatted(
                        countryId, expected, countryProbability
                    ));
        };
    }
}
