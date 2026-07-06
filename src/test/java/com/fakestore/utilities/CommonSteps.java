package com.fakestore.utilities;

import org.assertj.core.api.SoftAssertions;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class CommonSteps {

	private TestUtils tUtil = new TestUtils();
	private SoftAssertions softAssert = new SoftAssertions();
	public static Response procResp = null;
	public static String end_pt = null;
	static EnvironmentVariables env_var = SystemEnvironmentVariables.createEnvironmentVariables();
	String file_path = env_var.getProperty("json.body.path");
	public static ValidatableResponse response = null;

	public void verifyRequestSuccessful(Response response) {
		tUtil.verifyStatus(response, ResponseCode.OK);
	}

	public String buildQueryParameters(String attributeName, String value) {
		String[] attributes = attributeName.split(",");
		String[] values = value.split("/");

		StringBuilder queryParams = new StringBuilder("?");
		for (int i = 0; i < attributes.length; i++) {
			if (i > 0) {
				queryParams.append("&");
			}
			queryParams.append(attributes[i]).append("=").append(values[i]);
		}
		return queryParams.toString();
	}

	public String buildPostQueryParameters(String attributeName, String value) {
		String[] attributes = attributeName.split(",");
		String[] values = value.split("/");

		StringBuilder queryParams = new StringBuilder("?");
		for (int i = 0; i < attributes.length; i++) {
			if (i > 0) {
				queryParams.append("&");
			}
			queryParams.append(attributes[i]).append("=").append(values[i]);
		}
		return queryParams.toString();
	}

  public static String constructUrlWithParams(String baseUrl, String endpoint, String pathParams,
			String queryParams) {
		// Construct URL with path parameters
		String[] pathParamArray = pathParams.split("/");
		String pathParamString = String.join("/", pathParamArray);

		// Construct URL with query parameters
		String[] queryParamArray = queryParams.split(",");
		StringBuilder queryParamString = new StringBuilder();
		for (String queryParam : queryParamArray) {
			queryParamString.append(queryParam).append("&");
		}
		// Remove the trailing "&"
		if (queryParamString.length() > 0) {
			queryParamString.setLength(queryParamString.length() - 1);
		}

		// Combine base URL, endpoint, path parameters, and query parameters
		String fullUrl = baseUrl + endpoint + "/" + pathParamString;
		if (queryParamString.length() > 0) {
			fullUrl += "?" + queryParamString.toString();
		}

		return fullUrl;
	}
	public static String buildQueryString(String attributes, String values) {
        String[] keys = attributes.split(",");
        String[] vals = values.split("/");

        if (keys.length != vals.length) {
            throw new IllegalArgumentException("Mismatch in number of attributes and values.");
        }

        StringBuilder query = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i].trim();
            String val = vals[i].trim();
            if (i > 0) query.append("&");
            query.append(key).append("=").append(val);
        }
        return query.toString();
    }
	public static String buildUrl(String baseUrl, String endpoint, String param) {
        StringBuilder fullUrl = new StringBuilder(baseUrl);

        if (endpoint != null && !endpoint.isEmpty()) {
            if (!endpoint.startsWith("/")) {
                fullUrl.append("/");
            }
            fullUrl.append(endpoint);
        }

        if (param != null && !param.isEmpty()) {
            if (param.startsWith("%2F") || param.contains("%3F")) {
                fullUrl.append("/").append(param);
            } else {
                fullUrl.append("?").append(param);
            }
        }

        return fullUrl.toString();
    }

}
