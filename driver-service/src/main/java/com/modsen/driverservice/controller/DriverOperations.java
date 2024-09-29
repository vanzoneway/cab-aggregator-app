package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.Marker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
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
public interface DriverOperations {

    @PostMapping("/drivers")
    @Validated(Marker.OnCreate.class)
    DriverDto createDriver(@RequestBody DriverDto driverDto);

    @GetMapping("/drivers")
    Page<DriverDto> getPageDrivers(@RequestParam(defaultValue = "0") @Min(0) Integer offset,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @DeleteMapping("/drivers/{driverId}")
    ResponseEntity<String> deleteDriver(@PathVariable Long driverId);

    @PutMapping("/drivers/{driverId}")
    DriverDto updateDriverById(@PathVariable Long driverId, @RequestBody @Valid DriverDto driverDto);


}
