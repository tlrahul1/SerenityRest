package com.fakestore.utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class BaseClass {
	public static ResponseSpecBuilder respec;
	public static ResponseSpecification responseSpecification;

	public static RequestSpecification getGenericReqstSpecForProduct() {

		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

		String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("fakestore.api");

		return new RequestSpecBuilder().setBaseUri(baseUrl).setContentType("application/json").build();
	}

	public static RequestSpecification getqaGenericReqstSpecForProduct() {

		EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

		String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("fakestore.api");
		System.out.println("Base Url:" + baseUrl);
		return new RequestSpecBuilder().setBaseUri(baseUrl).setContentType("application/json").build();
	}
}
