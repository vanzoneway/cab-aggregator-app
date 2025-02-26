package com.modsen.passengerservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String BASE_URL = "http://localhost:8080/api/v1/passengers";
    public static final String ADMIN_AUTH_TOKEN_URL = "http://localhost:8080/api/v1/cab-aggregator/signin/admin";
    public static final String OFFSET_PARAM = "?offset=";
    public static final String LIMIT_PARAM = "&limit=";
    public static final String ID_POSTFIX = "/{id}";

    public static final String GRANT_TYPE = "client_credentials";
    public static final String CLIENT_ID = "admin-cli";
    public static final String CLIENT_SECRET = "VW0msamXP7l5NARSzDYwE22I8IF9s7Jo";

    public static final String ID_FIELD = "id";

}
