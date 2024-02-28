package api.tests.demo;

import api.tests.demo.config.ConfigContainer;
import api.tests.demo.constants.Endpoints;
import api.tests.demo.constants.Groups;
import api.tests.demo.requests.ServiceRequest;
import api.tests.demo.utils.AllureHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.function.Consumer;

import static api.tests.demo.validations.CommonValidations.*;
import static api.tests.demo.validations.NationalityByNameValidations.countriesSizeMustBeAtLeast;
import static api.tests.demo.validations.NationalityByNameValidations.countryProbabilityMustBeAtLeast;

public class NationalityByNameTests extends BaseTest {
    
    @DataProvider
    private Object[][] positiveTestsDataProvider() {
        return new Object[][] {
            { "Ivan", "UA", 0.04 },
            { "John", "GB", 0.04 },
            { "Ahmed", "SA", 0.08 }
        };
    }

    @Test(groups = { Groups.ALL, Groups.POSITIVE },
            dataProvider = "positiveTestsDataProvider")
    public void nationalityByNameTest(
        String name,
        String countryCode,
        double minProbability
    ) {
        AllureHelper.updateTestName("Nationality by name: '%s'".formatted(name));
        callEndpoint(name, combine(
            ok(),
            matchesJsonSchema("nationalityByNameSuccessfulResponse.json"),
            countriesSizeMustBeAtLeast(4),
            countryProbabilityMustBeAtLeast(countryCode, minProbability)
        ));
    }

    @DataProvider
    private Object[][] negativeTestsDataProvider() {
        return new Object[][] {
            { "Ivan 1" },
            { "_John_" },
            { "" },
            { "   " }
        };
    }

    @Test(groups = { Groups.ALL, Groups.NEGATIVE },
            dataProvider = "negativeTestsDataProvider")
    public void nationalityByNameInvalidNamesTest(String name) {
        AllureHelper.updateTestName("Nationality by name: '%s'".formatted(name));
        callEndpoint(name, clientError());
    }

    @Step("Call endpoint")
    private void callEndpoint(String name, Consumer<Response> validator) {
        Response response = new ServiceRequest(
            ConfigContainer.getConfig().getNationalityByNameServiceHost(),
            Endpoints.GET_NATIONALITY_BY_NAME
        ).queryParams(Map.of("name", name))
        .GET();
        validator.accept(response);
    }
}
