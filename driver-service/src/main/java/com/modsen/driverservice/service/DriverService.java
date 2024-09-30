package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.DriverDto;
import org.springframework.data.domain.Page;



public interface DriverService {

    DriverDto createDriver(DriverDto driverDto);

    Page<DriverDto> getPageDrivers(Integer offset, Integer limit);

    String safeDeleteDriverByDriverId(Long driverId);

    DriverDto updateDriverById(Long driverId, DriverDto driverDto);

}
