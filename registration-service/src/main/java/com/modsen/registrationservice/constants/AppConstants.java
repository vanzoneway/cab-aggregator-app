package com.modsen.registrationservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

    public static final String ENUM_TYPE_INVALID_MESSAGE_KEY = "enum.type.invalid";

    public static final String SERVICE_UNAVAILABLE_MESSAGE_KEY = "service.unavailable";

    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";

    public static final String PHONE_PATTERN = "^\\+?[1-9][0-9]{7,14}$";

}
