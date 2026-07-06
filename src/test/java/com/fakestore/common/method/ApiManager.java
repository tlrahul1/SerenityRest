package com.fakestore.common.method;

import java.io.File;

import com.fakestore.utilities.CommonUrlHandler;

import io.restassured.response.ValidatableResponse;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class ApiManager {

    private static final EnvironmentVariables ENV = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String JSON_BASE_PATH = getRequiredProperty("json.body.path");

    private static String getRequiredProperty(String key) {
        String value = EnvironmentSpecificConfiguration.from(ENV).getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing property in serenity config : " + key);
        }

        return value;
    }

    public ValidatableResponse postApi(String endpointKey) {
        String payloadFile = getRequiredProperty(endpointKey + ".path");
        return executePostRequest(payloadFile, endpointKey);
    }

    public ValidatableResponse postNegativeApi(String updatedPayloadFile, String endpointKey) {
        return executePostRequest(updatedPayloadFile, endpointKey);
    }

    public ValidatableResponse getApi(String endpointKey) {
        return executeGetRequest(endpointKey, null);
    }

    public ValidatableResponse getApiWithQueryParams(String endpointKey, String queryParams) {
        return executeGetRequest(endpointKey, queryParams);
    }

    public ValidatableResponse putApi(String endpointKey) {
        String payloadFile = getRequiredProperty(endpointKey + ".path");
        return executePutRequest(payloadFile, endpointKey);
    }

    public ValidatableResponse putNegativeApi(String updatedPayloadFile, String endpointKey) {
        return executePutRequest(updatedPayloadFile, endpointKey);
    }

    public ValidatableResponse deleteApi(String endpointKey) {
        return executeDeleteRequest(endpointKey);
    }

    private ValidatableResponse executePostRequest(String payloadFileName, String endpointKey) {

        File payloadFile = new File(JSON_BASE_PATH + "/" + payloadFileName);
        String url = CommonUrlHandler.getProductUrlByKey(endpointKey);
        System.out.println("================================================");
        System.out.println("API URL       : " + url);
        System.out.println("Payload File  : " + payloadFile.getAbsolutePath());
        System.out.println("================================================");

        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .body(payloadFile)
                .log().all()
                .when()
                .post(url)
                .then()
                .log().all();
    }

    private ValidatableResponse executeGetRequest(String endpointKey, String queryParams) {

        String url = CommonUrlHandler.getProductUrlByKey(endpointKey);
        if (queryParams != null && !queryParams.isEmpty()) {
            url = url + queryParams;
        }
        System.out.println("================================================");
        System.out.println("API URL       : " + url);
        System.out.println("================================================");

        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all();
    }

    private ValidatableResponse executePutRequest(String payloadFileName, String endpointKey) {

        File payloadFile = new File(JSON_BASE_PATH + "/" + payloadFileName);
        String url = CommonUrlHandler.getProductUrlByKey(endpointKey);
        System.out.println("================================================");
        System.out.println("API URL       : " + url);
        System.out.println("Payload File  : " + payloadFile.getAbsolutePath());
        System.out.println("================================================");

        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .body(payloadFile)
                .log().all()
                .when()
                .put(url)
                .then()
                .log().all();
    }

    private ValidatableResponse executeDeleteRequest(String endpointKey) {

        String url = CommonUrlHandler.getProductUrlByKey(endpointKey);
        System.out.println("================================================");
        System.out.println("API URL       : " + url);
        System.out.println("================================================");

        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .delete(url)
                .then()
                .log().all();
    }
}
