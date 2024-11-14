package com.modsen.passengerservice.e2e;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class E2ETestData {

    public static final String BASE_URL = "http://localhost:8080/api/v1/passengers";
    public static final String OFFSET_PARAM = "?offset=";
    public static final String LIMIT_PARAM = "&limit=";
    public static final String ID_POSTFIX = "/{id}";

}
