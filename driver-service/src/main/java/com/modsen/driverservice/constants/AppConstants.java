package com.modsen.driverservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final  class AppConstants {

    // Application exception messages keys
    public static final String CAR_NUMBER_DUPLICATE_MESSAGE_KEY = "car.number.duplicate";
    public static final String CAR_NOT_FOUND_MESSAGE_KEY = "car.not.found";
    public static final String RESTORE_CAR_MESSAGE_KEY = "restore.car.offer";
    public static final String DRIVER_NOT_FOUND_MESSAGE_KEY = "driver.not.found";
    public static final String DRIVER_EMAIL_DUPLICATE_MESSAGE_KEY = "driver.email.duplicate";
    public static final String DRIVER_PHONE_DUPLICATE_MESSAGE_KEY = "driver.phone.duplicate";
    public static final String RESTORE_DRIVER_BY_PHONE_MESSAGE_KEY = "restore.driver.by.phone.offer";
    public static final String RESTORE_DRIVER_BY_EMAIL_MESSAGE_KEY = "restore.driver.by.email.offer";
    public static final String UNSUPPORTED_FILE_TYPE_MESSAGE_KEY = "unsupported.file.type";
    public static final String NO_SUCH_AVATAR_MESSAGE_KEY = "no.such.avatar.exception";

    //Cache names
    public static final String CAR_CACHE_VALUE = "carCache";
    public static final String DRIVER_CACHE_VALUE = "driverCache";

}
