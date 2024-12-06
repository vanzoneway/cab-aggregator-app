package com.modsen.ratingservice.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface RideFeignClient {

    @GetMapping("/api/v1/rides/{rideId}")
    RideResponseDto findRideById(@PathVariable Long rideId,
                                 @RequestHeader("Accept-Language") String acceptLanguage,
                                 @RequestHeader("Authorization") String authorization);

}
