package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.Marker;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface CarOperations {

    @PostMapping("/cars/drivers/{driverId}")
    @Validated(Marker.OnCreate.class)
    CarDto createCar(@PathVariable Long driverId,
                     @RequestBody @Valid CarDto carDto);

    @PutMapping("/cars/{carId}/drivers/{driverId}")
    @Validated(Marker.OnUpdate.class)
    CarDto updateCarByCarIdAndDriverId(@PathVariable Long carId,
                                                       @PathVariable Long driverId,
                                                       @RequestBody @Valid CarDto carDto);

    @DeleteMapping("/cars/{carId}")
    ResponseEntity<String> safeDeleteCarById(@PathVariable Long carId);

    @GetMapping("/drivers/{driverId}/cars")
    DriverCarDto getDriverWithCars(@PathVariable Long driverId);


}
