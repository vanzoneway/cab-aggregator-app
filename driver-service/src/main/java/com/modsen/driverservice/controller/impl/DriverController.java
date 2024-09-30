package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.DriverOperations;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.service.CarService;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DriverController implements DriverOperations {

    private final DriverService driverService;

    @Override
    public DriverDto createDriver(@RequestBody @Valid DriverDto driverDto) {
        return driverService.createDriver(driverDto);
    }

    @Override
    public Page<DriverDto> getPageDrivers(@RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                          @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {
        return driverService.getPageDrivers(offset, limit);
    }

    @Override
    public ResponseEntity<String> safeDeleteDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.safeDeleteDriverByDriverId(driverId));
    }

    @Override
    public DriverDto updateDriverById(@PathVariable Long driverId, @RequestBody @Valid DriverDto driverDto) {
        return driverService.updateDriverById(driverId, driverDto);
    }

}
