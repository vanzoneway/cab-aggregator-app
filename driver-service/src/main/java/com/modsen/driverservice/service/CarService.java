package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.CarDto;

public interface CarService {

    CarDto createCar(Long driverId, CarDto car);

    CarDto updateCarByCarIdAndDriverId(Long carId, Long driverId, CarDto carDto);

    void safeDeleteCarByCarId(Long carId);

    CarDto getCarById(Long carId);

}