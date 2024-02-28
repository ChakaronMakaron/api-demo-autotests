package api.tests.demo;

import java.util.Map;
import java.util.function.Consumer;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.tests.demo.config.ConfigContainer;
import api.tests.demo.constants.Endpoints;
import api.tests.demo.constants.Groups;
import api.tests.demo.requests.ServiceRequest;
import api.tests.demo.utils.AllureHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.tests.demo.validations.CommonValidations.*;
import static api.tests.demo.validations.EvenOrOddValidations.*;

public class EvenOrOddTests extends BaseTest {

    @DataProvider
    private Object[][] positiveTestsDataProvider() {
        return new Object[][] {
            { 0, true },
            { 1, false },
            { 2, true }
        };
    }

    @Test(groups = { Groups.ALL, Groups.POSITIVE },
            dataProvider = "positiveTestsDataProvider")
    public void evenOrOddTest(int number, boolean isEven) {
        AllureHelper.updateTestName("Even or odd: %s".formatted(number));
        callEndpoint(number, combine(
            ok(),
            matchesJsonSchema("evenOrOddSuccessfulResponse.json"),
            isEven ? mustBeEven() : mustBeOdd()
        ));
    }

    @Test(groups = { Groups.ALL, Groups.NEGATIVE },
            description = "Negative numbers are not available")
    public void negativeNumbersUnavailableTest() {
        callEndpoint(-1, combine(
            clientError(),
            errorMustContain("Number out of range")
        ));
    }
    
    @Step("Call endpoint")
    private void callEndpoint(int number, Consumer<Response> validator) {
        Response response = new ServiceRequest(
            ConfigContainer.getConfig().getEvenOrOddServiceHost(),
            Endpoints.DEFINE_EVEN_OR_ODD
        ).pathParams(Map.of("number", String.valueOf(number)))
        .GET();
        validator.accept(response);
    }
}
