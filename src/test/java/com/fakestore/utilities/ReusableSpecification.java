package com.fakestore.utilities;

import java.net.URI;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class ReusableSpecification {

	static EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
	public static ResponseSpecBuilder respec;
	public static ResponseSpecification responseSpecification;

	public static RequestSpecBuilder rspec;
	public static RequestSpecification requestSpecification;

	public static RequestSpecification getGenericRequestSpec() {

		rspec = new RequestSpecBuilder();
		rspec.setContentType(ContentType.JSON);
		requestSpecification = rspec.build();
		return requestSpecification;
	}

	public static RequestSpecification getGenericRequestSpec(URI url) {

		requestSpecification = new RequestSpecBuilder().setBaseUri(url).build();
		rspec.setContentType(ContentType.JSON);
		requestSpecification = rspec.build();
		return requestSpecification;

	}

	public static RequestSpecification getGenericExpRequestSpec() {

		rspec = new RequestSpecBuilder();
		rspec.setContentType(ContentType.JSON);
		rspec.addHeader("Content-Type", "application/json");
		rspec.build();
		return requestSpecification;
	}

}
