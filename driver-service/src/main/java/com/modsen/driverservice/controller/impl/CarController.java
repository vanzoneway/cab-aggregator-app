package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.CarOperations;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.service.CarService;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class CarController implements CarOperations {

    private final DriverService driverService;
    private final CarService carService;

    @Override
    public CarDto createCar(@PathVariable Long driverId,
                            @RequestBody @Valid CarDto carDto) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateCarByCarIdAndDriverId(@PathVariable Long carId,
                                                              @PathVariable Long driverId,
                                                              @RequestBody @Valid CarDto carDto) {
        return null;
    }

    @Override
    public ResponseEntity<String> safeDeleteCarById(@PathVariable Long carId) {
        return null;
    }

    @Override
    public DriverCarDto getDriverWithCars(@PathVariable Long driverId,
                                          @RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                          @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {
        return null;
    }

}
