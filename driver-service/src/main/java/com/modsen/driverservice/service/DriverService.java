package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import org.springframework.data.domain.Page;

public interface DriverService {

    DriverDto createDriver(DriverDto driverDto);

    ListContainerResponseDto<DriverDto> getPageDrivers(Integer offset, Integer limit);

    void safeDeleteDriverByDriverId(Long driverId);

    DriverDto updateDriverById(Long driverId, DriverDto driverDto);

    DriverDto getDriverById(Long driverId);

}
