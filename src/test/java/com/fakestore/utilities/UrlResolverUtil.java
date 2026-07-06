package com.fakestore.utilities;

import java.util.Map;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

public class UrlResolverUtil {

    public static void mapTo(String propertyKey, String[] mappings, Map<String, String> endpointMap, String webserviceUrl, EnvironmentVariables envVars) {
        String basePath = getRequiredProperty(propertyKey, envVars);
        for (String entry : mappings) {
            String[] parts = entry.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid mapping format: " + entry);
            }
            String key = parts[0].toLowerCase();
            String endpoint = parts[1];
            String fullUrl = webserviceUrl + basePath;
            if (!endpoint.isEmpty()) {
                fullUrl += endpoint;
            }
            endpointMap.put(key, fullUrl);
        }
    }

    public static String getRequiredProperty(String key, EnvironmentVariables envVars) {
        String value = EnvironmentSpecificConfiguration.from(envVars).getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing property for key: " + key);
        }
        return value;
    }

    public static String getUrlByKey(String commonEndpt, Map<String, String> endpointMap) {
        String url = endpointMap.get(commonEndpt.toLowerCase());
        if (url == null) {
            throw new IllegalArgumentException("Unknown endpoint key: " + commonEndpt);
        }
        return url;
    }
}
