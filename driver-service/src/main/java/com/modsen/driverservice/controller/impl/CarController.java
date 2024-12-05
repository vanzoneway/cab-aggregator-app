package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.CarOperations;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController implements CarOperations {

    private final CarService carService;

    @Override
    @PostMapping("/drivers/{driverId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public CarDto createCar(@PathVariable Long driverId,
                            @RequestBody @Valid CarDto carDto) {
        return carService.createCar(driverId, carDto);
    }

    @Override
    @PutMapping("/{carId}/drivers/{driverId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public CarDto updateCarByCarIdAndDriverId(@PathVariable Long carId,
                                              @PathVariable Long driverId,
                                              @RequestBody @Valid CarDto carDto) {
        return carService.updateCarByCarIdAndDriverId(carId, driverId, carDto);
    }

    @Override
    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public void safeDeleteCarById(@PathVariable Long carId) {
        carService.safeDeleteCarByCarId(carId);
    }

    @Override
    @GetMapping("/{carId}")
    public CarDto getCarById(@PathVariable Long carId) {
        return carService.getCarById(carId);
    }

}
