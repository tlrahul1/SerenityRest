package com.fakestore.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.json.simple.JSONObject;
import java.io.File;


public class JsonUtils {

	EnvironmentVariables envVar = SystemEnvironmentVariables.createEnvironmentVariables();
    String filePath = envVar.getProperty("json.body.path");
    public static final Map<String, Object> var=new HashMap<String, Object>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static String procJson = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    private static TestUtils tUtil = new TestUtils();

	public static void add(String key, Object value)
	{
		var.put(key, value);
	}

	public static Object get(String key)
	{
		return var.get(key);
	}

	 public String getFromJSON(String fileName, String pathJson) {
	        String path = filePath + "/" + fileName;
	        File file = new File(path);
	        String jso = "";
	        try {
	            JSONObject root = mapper.readValue(file, JSONObject.class);
	            jso = root.toJSONString();

	            Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
	                    .mappingProvider(new JacksonMappingProvider()).build();

	            JsonNode updatedJson = com.jayway.jsonpath.JsonPath.using(configuration).parse(jso).read(pathJson);
	            jso = updatedJson.toString().replaceAll("\"", "");

	        } catch (Exception e) {
	            System.out.println("Exception executing the function ......!!" + e.getStackTrace());
	        }
	        return jso;
	    }

}
