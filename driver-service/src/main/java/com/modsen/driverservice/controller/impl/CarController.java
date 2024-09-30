package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.CarOperations;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.service.CarService;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController implements CarOperations {

    private final CarService carService;

    @Override
    public CarDto createCar(@PathVariable Long driverId,
                            @RequestBody @Valid CarDto carDto) {
        return carService.createCar(driverId, carDto);
    }

    @Override
    public CarDto updateCarByCarIdAndDriverId(@PathVariable Long carId,
                                              @PathVariable Long driverId,
                                              @RequestBody @Valid CarDto carDto) {
        return carService.updateCarByCarIdAndDriverId(carId, driverId, carDto);
    }

    @Override
    public ResponseEntity<String> safeDeleteCarById(@PathVariable Long carId) {
        return ResponseEntity.ok(carService.safeDeleteCarByCarId(carId));
    }

    @Override
    public DriverCarDto getDriverWithCars(@PathVariable Long driverId) {
        return carService.getDriverWithCars(driverId);
    }

}
