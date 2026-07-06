package com.fakestore.common.steps;

import java.io.FileNotFoundException;

import com.fakestore.common.method.ApiManager;
import com.fakestore.utilities.ResponseValidator;
import com.fakestore.utilities.TestUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class CommonApiSteps {

    @Steps
    ApiManager apiManager;

    private static final EnvironmentVariables ENV = SystemEnvironmentVariables.createEnvironmentVariables();
    private final TestUtils testUtils = new TestUtils();
    private Response response;

    @Given("user call web POST API with {string} endpoint")
    public void userCallWebPostApiWithEndpoint(String endpoint) {
        response = apiManager.postApi(endpoint).extract().response();
    }

    @Given("user call web GET API with {string} endpoint")
    public void userCallWebGetApiWithEndpoint(String endpoint) {
        response = apiManager.getApi(endpoint).extract().response();
    }

    @Given("user call web GET API with {string} endpoint and query params {string}")
    public void userCallWebGetApiWithEndpointAndQueryParams(String endpoint, String queryParams) {
        response = apiManager.getApiWithQueryParams(endpoint, queryParams).extract().response();
    }

    @Given("user call web PUT API with {string} endpoint")
    public void userCallWebPutApiWithEndpoint(String endpoint) {
        response = apiManager.putApi(endpoint).extract().response();
    }

    @Given("user call web DELETE API with {string} endpoint")
    public void userCallWebDeleteApiWithEndpoint(String endpoint) {
        response = apiManager.deleteApi(endpoint).extract().response();
    }

    @Then("user verifies the web api expected status code as {string}")
    public void userVerifiesTheWebApiExpectedStatusCodeAs(String expectedStatusCode) {
        ResponseValidator.verifyHttpStatus(response, Integer.parseInt(expectedStatusCode));
    }

    @When("user call web api POST API after modifying {string} with {string} with endpoint {string},{string} and {string}")
    public void userCallWebApiPostApiAfterModifying(String attributeName, String value, String requestKey,
            String operation, String dataType) throws FileNotFoundException {
        String readFile = ENV.getProperty(requestKey + ".path");
        String updatedFile = ENV.getProperty("update" + requestKey + ".path");
        String jsonBasePath = ENV.getProperty("json.body.path");
        testUtils.update_JsonArrayValueInteger(jsonBasePath + "/" + readFile, updatedFile, attributeName, value,
                operation, dataType);
        response = apiManager.postNegativeApi(updatedFile, requestKey).extract().response();
    }

    @Then("user verifies the web api Expected status code as {string} and Response Message as {string}")
    public void userVerifiesTheWebApiExpectedStatusCodeAsAndResponseMessageAs(String expectedStatus,
            String expectedMessage) {
        ResponseValidator.verifyHttpStatus(response, 400);
        ResponseValidator.verifyResponseMessage(response, expectedStatus, expectedMessage);
    }
}
