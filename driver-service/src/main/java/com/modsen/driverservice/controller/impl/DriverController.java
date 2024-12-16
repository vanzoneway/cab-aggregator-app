package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.aspect.ValidateAccessToResources;
import com.modsen.driverservice.controller.DriverOperations;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController implements DriverOperations {

    private final DriverService driverService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DriverDto createDriver(@RequestBody @Valid DriverDto driverDto) {
        return driverService.createDriver(driverDto);
    }

    @Override
    @GetMapping
    public ListContainerResponseDto<DriverDto> getPageDrivers(@RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {
        return driverService.getPageDrivers(offset, limit);
    }

    @Override
    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @ValidateAccessToResources
    public void safeDeleteDriver(@PathVariable Long driverId, JwtAuthenticationToken jwtAuthenticationToken) {
        driverService.safeDeleteDriverByDriverId(driverId);
    }

    @Override
    @PutMapping("/{driverId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @ValidateAccessToResources
    public DriverDto updateDriverById(@PathVariable Long driverId, @RequestBody @Valid DriverDto driverDto,
                                      JwtAuthenticationToken jwtAuthenticationToken) {
        return driverService.updateDriverById(driverId, driverDto);
    }

    @Override
    @GetMapping("/{driverId}")
    public DriverDto getDriverById(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId);
    }

    @Override
    @GetMapping("/{driverId}/cars")
    public DriverCarDto getDriverWithCars(@PathVariable Long driverId) {
        return driverService.getDriverWithCars(driverId);
    }

}
