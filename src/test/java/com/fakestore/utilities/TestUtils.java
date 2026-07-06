package com.fakestore.utilities;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestUtils {

	public static List<String> put_Vals = new ArrayList<String>();
	private static final EnvironmentVariables envVar = SystemEnvironmentVariables.createEnvironmentVariables();
	public static String filePath = envVar.getProperty("json.body.path");
	SoftAssertions softAssert = new SoftAssertions();
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

	String file_path = envVar.getProperty("json.body.path");
	File file = new File(file_path);

	public ObjectMapper mapper = new ObjectMapper();

	public void responseContainsTheString(Response response, String field) {
		String bodyAsString = response.getBody().asString();
		Assert.assertTrue(bodyAsString.contains(field));
	}

	public void verifyStatus(Response res, int status) {
		int code = res.getStatusCode();
		assertEquals(status, code);
		LOGGER.info("Status Code:" + code);
	}

	public void verifyScope(Response resp) {
		softAssert.assertThat(resp.jsonPath().getString("scope")).isEqualTo("APP");
	}

	public void verifyTrueSuccess(Response resp) {
		softAssert.assertThat(resp.jsonPath().getString("successFlag")).isEqualTo("true");
	}

	public void verifyReqSuccessMsg(Response resp) {
		softAssert.assertThat(resp.jsonPath().getString("message")).isEqualTo("Request successful");
	}

	public void verifySaveSuccessMsg(Response resp) {
		softAssert.assertThat(resp.jsonPath().getString("returnMessage")).isEqualTo("Saved Successfully");
	}

	public Map<String, String> store_jsonValues(Response res, List<String> storeValue) {

		io.restassured.path.json.JsonPath jsonPathEvaluator = res.jsonPath();
		Map<String, String> storedValues = new HashMap<String, String>();

		for (int i = 0; i < storeValue.size(); i++) {
			String value = jsonPathEvaluator.getString(storeValue.get(i));
			storedValues.put(storeValue.get(i), value);
		}

		System.out.println("Stored Values " + storedValues);

		return storedValues;
	}

	public static void putToSession(Object key, Object value) {
		Serenity.getCurrentSession().put(key, value);
	}

	public static Object extractFrom(Response res, String query) {
		return res.jsonPath().get(query);
	}

	public static String readJsonFromFile(String filePath) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		return content;
	}

	public String modifyJsonField(String jsonstring, String field, Object newValue) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(field, newValue);
		return jsonObject.toString();
	}

	public void update_JSONValue(String fileName, String JsonPath, String replaceValue) throws Exception {
		String path = filePath + "/" + fileName;
		File file = new File(path);
		FileReader reader = new FileReader(file);

		try {
			JSONObject root = mapper.readValue(file, JSONObject.class);

			String jso = root.toString();
			System.out.println("Jso print:" + jso);
			Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
					.mappingProvider(new JacksonMappingProvider()).build();
			System.out.println("configuration ---------------");

			JsonNode updatedJson = com.jayway.jsonpath.JsonPath.using(configuration).parse(jso)
					.set(JsonPath, replaceValue).json();
			System.out.println("updated json------------------------");

			jso = updatedJson.toString();
			System.out.println("-------------------------------------------------");
			System.out.println("updated json:/n" + jso);
			System.out.println("-------------------------------------------------");
			FileWriter fw = new FileWriter(file);
			try {
				fw.write(jso);
			} catch (IOException ioe) {
				System.out.println("Exception opening or accessing the file......!!" + ioe.getMessage());
				System.out.println("Exception opening or accessing the file......!!" + ioe.getStackTrace());

			} finally {
				fw.flush();
				fw.close();
				reader.close();
			}
		}

		catch (Exception e) {
			System.out.println("Exception executing the function ......!!" + e.getStackTrace() + "error message......!!"
					+ e.getMessage());
		}
	}

	public void update_JSONValue(String fileName, String JsonPath, int replaceValue) throws Exception {
		String path = filePath + "/" + fileName;
		File file = new File(path);
		FileReader reader = new FileReader(file);

		try {

			JSONObject root = mapper.readValue(file, JSONObject.class);

			String jso = root.toString();

			Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
					.mappingProvider(new JacksonMappingProvider()).build();

			JsonNode updatedJson = com.jayway.jsonpath.JsonPath.using(configuration).parse(jso)
					.set(JsonPath, replaceValue).json();

			jso = updatedJson.toString();

			FileWriter fw = new FileWriter(file);
			try {
				fw.write(jso);
			} catch (IOException ioe) {
				System.out.println("Exception opening or accessing the file......!!" + ioe.getMessage());
				System.out.println("Exception opening or accessing the file......!!" + ioe.getStackTrace());

			} finally {
				fw.flush();
				fw.close();
				reader.close();
			}
		}

		catch (Exception e) {
			System.out.println("Exception executing the function ......!!" + e.getStackTrace());
		} finally {

		}
	}

	public void updateJsonValue(String fileName, File file, String attribute, String replaceValue) throws IOException {
		Map<String, Object> userData = mapper.readValue(file, new TypeReference<Map<String, Object>>() {
		});

		System.out.println("Response-----------" + userData.toString());
		try {
			String path = filePath + "/" + fileName;
			String jsonContent = new String(Files.readAllBytes(Paths.get(path)));

			JSONObject jsonObject = new JSONObject(jsonContent);

			jsonObject.put(attribute, replaceValue);

			Files.write(Paths.get(path), jsonObject.toString(4).getBytes());

			System.out.println("Attribute has been updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update_JsonValue(String readFromFile, String writeToFile, String attribute, String replaceValue,
			String operation) throws FileNotFoundException {
		String fileToReadPath = readFromFile;
		try {
			String fileToWritePath = filePath + "/" + writeToFile;
			Files.copy(Paths.get(fileToReadPath), Paths.get(fileToWritePath), StandardCopyOption.REPLACE_EXISTING);
			String jsonContent = new String(Files.readAllBytes(Paths.get(fileToWritePath)));

			JSONObject jsonObject = new JSONObject(jsonContent);
			if (operation.toLowerCase().equals("remove")) {
				System.out.println("removing----------------");
				jsonObject.remove(attribute);
			}

			else {
				jsonObject.put(attribute, replaceValue);
			}

			Files.write(Paths.get(fileToWritePath), jsonObject.toString(4).getBytes());

			System.out.println("Attribute has been updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update_JsonValueInteger(String readFromFile, String writeToFile, String attribute, String replaceValue,
			String operation, String dataType) throws FileNotFoundException {
		String fileToReadPath = readFromFile;
		try {
			String fileToWritePath = filePath + "/" + writeToFile;
			Files.copy(Paths.get(fileToReadPath), Paths.get(fileToWritePath), StandardCopyOption.REPLACE_EXISTING);
			String jsonContent = new String(Files.readAllBytes(Paths.get(fileToWritePath)));

			JSONObject jsonObject = new JSONObject(jsonContent);

			if (operation.equalsIgnoreCase("remove") && dataType.equalsIgnoreCase("data")) {
				System.out.println("Removing attribute: " + attribute);
				removeNestedAttribute(jsonObject, attribute);

			} else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("String")) {
				System.out.println("Changing value to String for attribute: " + attribute);
				updateNestedAttribute(jsonObject, attribute, replaceValue);
			} else if (operation.equalsIgnoreCase("changevalue") && dataType.equalsIgnoreCase("Integer")) {
				System.out.println("Changing value to Integer for attribute: " + attribute);
				int intValue = Integer.parseInt(replaceValue);
				updateNestedAttribute(jsonObject, attribute, intValue);

			} else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("Double")) {
				System.out.println("Changing value to Double for attribute: " + attribute);
				double doubleValue = Double.parseDouble(replaceValue);
				updateNestedAttribute(jsonObject, attribute, doubleValue);
			}

			Files.write(Paths.get(fileToWritePath), jsonObject.toString(4).getBytes());

			System.out.println("Values have been updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateNestedAttribute(JSONObject jsonObject, String attribute, Object value) {
		if (attribute.contains(".")) {
			String[] parts = attribute.split("\\.");
			JSONObject nestedObject = jsonObject;
			for (int i = 0; i < parts.length - 1; i++) {
				String part = parts[i];
				if (part.endsWith("]")) {
					int arrayIndex = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
					part = part.substring(0, part.indexOf("["));
					if (nestedObject.has(part)) {
						nestedObject = nestedObject.getJSONArray(part).getJSONObject(arrayIndex);
					}
				} else {
					if (nestedObject.has(part)) {
						nestedObject = nestedObject.getJSONObject(part);
					}
				}
			}
			nestedObject.put(parts[parts.length - 1], value);
		} else {
			jsonObject.put(attribute, value);
		}
	}

	private void removeNestedAttribute(JSONObject jsonObject, String attribute) {
		if (attribute.contains(".")) {
			String[] parts = attribute.split("\\.");
			JSONObject nestedObject = jsonObject;
			for (int i = 0; i < parts.length - 1; i++) {
				String part = parts[i];
				if (part.endsWith("]")) {
					int arrayIndex = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
					part = part.substring(0, part.indexOf("["));
					if (nestedObject.has(part)) {
						nestedObject = nestedObject.getJSONArray(part).getJSONObject(arrayIndex);
					}
				} else {
					if (nestedObject.has(part)) {
						nestedObject = nestedObject.getJSONObject(part);
					}
				}
			}
			nestedObject.remove(parts[parts.length - 1]);
		} else {
			jsonObject.remove(attribute);
		}
	}

	public void update_JsonArrayValueInteger(String readFromFile, String writeToFile, String attribute, String replaceValue,
	        String operation, String dataType) throws FileNotFoundException {
	    String fileToReadPath = readFromFile;
	    try {
	        String fileToWritePath = filePath + "/" + writeToFile;
	        Files.copy(Paths.get(fileToReadPath), Paths.get(fileToWritePath), StandardCopyOption.REPLACE_EXISTING);
	        String jsonContent = new String(Files.readAllBytes(Paths.get(fileToWritePath)));

	        if (jsonContent.trim().startsWith("[")) {
	            JSONArray jsonArray = new JSONArray(jsonContent);

	            if (operation.equalsIgnoreCase("remove") && dataType.equalsIgnoreCase("data")) {
	                System.out.println("Removing attribute: " + attribute);
	                removeNestedAttribute(jsonArray, attribute);

	            } else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("String")) {
	                System.out.println("Changing value to String for attribute: " + attribute);
	                updateNestedAttribute(jsonArray, attribute, replaceValue);

	            } else if (operation.equalsIgnoreCase("changevalue") && dataType.equalsIgnoreCase("Integer")) {
	                System.out.println("Changing value to Integer for attribute: " + attribute);
	                int intValue = Integer.parseInt(replaceValue);
	                updateNestedAttribute(jsonArray, attribute, intValue);

	            } else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("Double")) {
	                System.out.println("Changing value to Double for attribute: " + attribute);
	                double doubleValue = Double.parseDouble(replaceValue);
	                updateNestedAttribute(jsonArray, attribute, doubleValue);
	            }

	            Files.write(Paths.get(fileToWritePath), jsonArray.toString(4).getBytes());

	        } else if (jsonContent.trim().startsWith("{")) {
	            JSONObject jsonObject = new JSONObject(jsonContent);

	            if (operation.equalsIgnoreCase("remove") && dataType.equalsIgnoreCase("data")) {
	                System.out.println("Removing attribute: " + attribute);
	                removeNestedAttribute(jsonObject, attribute);

	            } else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("String")) {
	                System.out.println("Changing value to String for attribute: " + attribute);
	                updateNestedAttribute(jsonObject, attribute, replaceValue);

	            } else if (operation.equalsIgnoreCase("changevalue") && dataType.equalsIgnoreCase("Integer")) {
	                System.out.println("Changing value to Integer for attribute: " + attribute);
	                int intValue = Integer.parseInt(replaceValue);
	                updateNestedAttribute(jsonObject, attribute, intValue);

	            } else if (operation.equalsIgnoreCase("modify") && dataType.equalsIgnoreCase("Double")) {
	                System.out.println("Changing value to Double for attribute: " + attribute);
	                double doubleValue = Double.parseDouble(replaceValue);
	                updateNestedAttribute(jsonObject, attribute, doubleValue);
	            }

	            Files.write(Paths.get(fileToWritePath), jsonObject.toString(4).getBytes());
	        }

	        System.out.println("Values have been updated.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void updateNestedAttribute(JSONArray jsonArray, String attribute, Object value) {
	    for (int i = 0; i < jsonArray.length(); i++) {
	        JSONObject jsonObject = jsonArray.getJSONObject(i);
	        updateNestedAttribute(jsonObject, attribute, value);
	    }
	}

	private void removeNestedAttribute(JSONArray jsonArray, String attribute) {
	    for (int i = 0; i < jsonArray.length(); i++) {
	        JSONObject jsonObject = jsonArray.getJSONObject(i);
	        removeNestedAttribute(jsonObject, attribute);
	    }
	}

}
