package com.fakestore.utilities;

import org.assertj.core.api.SoftAssertions;

import io.restassured.response.Response;

public class ResponseValidator {
	 private ResponseValidator() {
	        // Prevent object creation
	    }

	    public static void verifyHttpStatus(Response response,int expectedStatusCode) {

	       SoftAssertions softAssert = new SoftAssertions();
           softAssert.assertThat(response.getStatusCode()).as("Validate HTTP Status Code").isEqualTo(expectedStatusCode);
           softAssert.assertAll();
	    }

	    public static void verifyResponseMessage(Response response,String expectedStatus,String expectedMessage) {
            SoftAssertions softAssert = new SoftAssertions();
            softAssert.assertThat(response.jsonPath().getString("status")).as("Validate API Status").isEqualTo(expectedStatus);
            softAssert.assertThat(response.jsonPath().getString("errors")).as("Validate Error Message").contains(expectedMessage);
            softAssert.assertAll();
	    }

}
