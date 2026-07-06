package com.fakestore.utilities;

import io.restassured.RestAssured;

public class RestUtils {

	 public RestUtils(String uurl) {
	    	RestAssured.baseURI = uurl;
		}

	    public RestUtils() {

	  	}
		public static void setBaseURI (String baseURI){
	        RestAssured.baseURI = baseURI;
	        RestAssured.urlEncodingEnabled = false;
	    }

	    /*
	    ***Sets base path***
	    Before starting the test, we should set the RestAssured.basePath
	    */
	    public static void setBasePath(String basePathTerm){
	        RestAssured.basePath = basePathTerm;
	    }

	    /*
	    ***Reset Base URI (after test)***
	    After the test, we should reset the RestAssured.baseURI
	    */
	    public static void resetBaseURI (){
	        RestAssured.baseURI = null;
	    }

	    /*
	    ***Reset base path (after test)***
	    After the test, we should reset the RestAssured.basePath
	    */
	    public static void resetBasePath(){
	        RestAssured.basePath = null;
	    }


}
