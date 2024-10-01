package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.controller.DriverOperations;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DriverController implements DriverOperations {

    private final DriverService driverService;

    @Override
    @PostMapping("/drivers")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDto createDriver(@RequestBody @Valid DriverDto driverDto) {
        return driverService.createDriver(driverDto);
    }

    @Override
    @GetMapping("/drivers")
    public ListContainerResponseDto<DriverDto> getPageDrivers(@RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {
        return driverService.getPageDrivers(offset, limit);
    }

    @Override
    @DeleteMapping("/drivers/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void safeDeleteDriver(@PathVariable Long driverId) {
        driverService.safeDeleteDriverByDriverId(driverId);
    }

    @Override
    @PutMapping("/drivers/{driverId}")
    public DriverDto updateDriverById(@PathVariable Long driverId, @RequestBody @Valid DriverDto driverDto) {
        return driverService.updateDriverById(driverId, driverDto);
    }

    @Override
    @GetMapping("/drivers/{driverId}")
    public DriverDto getDriverById(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId);
    }

}
