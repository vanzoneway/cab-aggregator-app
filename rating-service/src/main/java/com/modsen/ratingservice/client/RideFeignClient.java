package com.modsen.ratingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//TODO solve issue with headers
@FeignClient(value = "rides-service")
public interface RideFeignClient {

    @GetMapping("/api/v1/rides/{rideId}")
    RideResponseDto findRideById(@PathVariable Long rideId);

}
