package com.fakestore.utilities;

import java.io.InputStream;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLoader {

	 private static Map<String, Object> jsonData;

	    static {
	        try {
	            InputStream inputStream = JsonLoader.class.getClassLoader().getResourceAsStream("testData.json");
	            ObjectMapper objectMapper = new ObjectMapper();
	            jsonData = objectMapper.readValue(inputStream, Map.class);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to load test data");
	        }
	    }

	    public static Map<String, Object> getTestData(String scenario) {
	        return (Map<String, Object>) ((Map<String, Object>) jsonData.get("productDashboardData")).get(scenario);
	    }

}
