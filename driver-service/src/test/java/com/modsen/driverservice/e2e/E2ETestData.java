package com.modsen.driverservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String DRIVER_BASE_URL = "http://localhost:8080/api/v1/drivers";
    public static final String CAR_BASE_URL = "http://localhost:8080/api/v1/cars";
    public static final String UPDATE_CAR_POSTFIX = "/{carId}/drivers/{driverId}";
    public static final String CREATE_CAR_POSTFIX = "/drivers/{id}";
    public static final String ID_POSTFIX = "/{id}";

}
