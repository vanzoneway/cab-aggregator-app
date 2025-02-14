package com.modsen.driverservice.client.ride;

import com.modsen.driverservice.client.ride.dto.RideStatisticResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface RideFeignClient {

    @GetMapping("/api/v1/rides/drivers/{driverId}/statistics")
    RideStatisticResponseDto getRideStatisticForDriver(@PathVariable("driverId") Long passengerId);

}
