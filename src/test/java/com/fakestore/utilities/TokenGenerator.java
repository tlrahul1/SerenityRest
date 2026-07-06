package com.fakestore.utilities;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

/**
 * NOTE: The original MaxHealthcare TokenGenerator hardcoded live JWT bearer
 * tokens directly in source. That is a credential-leak anti-pattern and is
 * NOT reproduced here. This generic replacement keeps the same method shape
 * but resolves the token from serenity config / environment variables, so
 * secrets never live in version control.
 *
 * Configure via serenity.conf, e.g.:
 *   serenity.auth.token = ${FAKESTORE_BEARER_TOKEN}
 */
public class TokenGenerator {

    private static final EnvironmentVariables ENV = SystemEnvironmentVariables.createEnvironmentVariables();

    public static String getBearerToken() {
        String token = EnvironmentSpecificConfiguration.from(ENV).getProperty("serenity.auth.token");
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing bearer token. Set 'serenity.auth.token' in serenity.conf or export FAKESTORE_BEARER_TOKEN.");
        }
        return token;
    }
}
