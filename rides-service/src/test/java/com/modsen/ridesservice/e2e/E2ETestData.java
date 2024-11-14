package com.modsen.ridesservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String BASE_URL = "http://localhost:8080/api/v1/rides";
    public static final String UPDATE_RIDE_STATUS_POSTFIX = "/{id}/status";
    public static final String RIDE_ID_POSTFIX = "/{id}";

    public static final String ORDER_DATE_TIME_FIELD = "orderDateTime";
    public static final String COST_FIELD = "cost";

}
