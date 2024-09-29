package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.Marker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface CarOperations {

    @PostMapping("/cars/drivers/{driverId}")
    @Validated(Marker.OnCreate.class)
    CarDto createCar(@PathVariable Long driverId,
                     @RequestBody @Valid CarDto carDto);

    @PutMapping("/cars/{carId}/drivers/{driverId}")
    ResponseEntity<String> updateCarByCarIdAndDriverId(@PathVariable Long carId,
                                                       @PathVariable Long driverId,
                                                       @RequestBody @Valid CarDto carDto);

    @DeleteMapping("/cars/{carId}")
    ResponseEntity<String> safeDeleteCarById(@PathVariable Long carId);

    @GetMapping("/drivers/{driverId}/cars")
    DriverCarDto getDriverWithCars(@PathVariable Long driverId,
                                   @RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);


}
