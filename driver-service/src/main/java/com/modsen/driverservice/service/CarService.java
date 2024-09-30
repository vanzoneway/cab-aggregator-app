package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;


public interface CarService {

    CarDto createCar(Long driverId, CarDto car);

    CarDto updateCarByCarIdAndDriverId(Long carId, Long driverId, CarDto carDto);

    String safeDeleteCarByCarId(Long carId);

    DriverCarDto getDriverWithCars(Long driverId);

}