package com.modsen.ratingservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String BASE_URL = "http://localhost:8080/api/v1/drivers-ratings";
    public static final String AVERAGE_RATING_POSTFIX = "/{id}/average";
    public static final String ID_POSTFIX = "/{id}";
}
