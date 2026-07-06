package com.fakestore.utilities;

import java.util.HashMap;
import java.util.Map;

import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class CommonUrlHandler {

    private static final EnvironmentVariables envVar = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String webserviceUrl = UrlResolverUtil.getRequiredProperty("fakestore.api", envVar);
    private static final Map<String, String> endpointMap = new HashMap<>();

    // Product_Module
    static {
        UrlResolverUtil.mapTo("product.master", new String[] {
                "getproducts:",
                "createproduct:",
                "updateproduct:/1",
                "deleteproduct:/1"
        }, endpointMap, webserviceUrl, envVar);
    }

    public static String getProductUrlByKey(String commonEndpt) {
        return UrlResolverUtil.getUrlByKey(commonEndpt, endpointMap);
    }
}
