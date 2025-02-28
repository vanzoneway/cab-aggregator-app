package com.modsen.driverservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String DRIVER_BASE_URL = "http://localhost:8080/api/v1/drivers";
    public static final String CAR_BASE_URL = "http://localhost:8080/api/v1/cars";
    public static final String ADMIN_AUTH_TOKEN_URL = "http://localhost:8080/api/v1/cab-aggregator/signin/admin";
    public static final String UPDATE_CAR_POSTFIX = "/{carId}/drivers/{driverId}";
    public static final String CREATE_CAR_POSTFIX = "/drivers/{id}";
    public static final String ID_POSTFIX = "/{id}";

    public static final String GRANT_TYPE = "client_credentials";
    public static final String CLIENT_ID = "admin-cli";
    public static final String CLIENT_SECRET = "VW0msamXP7l5NARSzDYwE22I8IF9s7Jo";

    public static final String ID_FIELD = "id";

}
