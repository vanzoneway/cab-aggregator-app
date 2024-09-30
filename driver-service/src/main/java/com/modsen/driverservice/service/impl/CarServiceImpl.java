package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.mapper.CarMapper;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.CarRepository;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final DriverMapper driverMapper;
    private final DriverRepository driverRepository;

    @Override
    public CarDto createCar(Long driverId, CarDto carDto) {

        if(carRepository.existsByNumberAndDeletedIsFalse(carDto.number())) {
            throw new DuplicateCarNumbersException("Car number " + carDto.number() + " already exists");
        }
        Car carEntity = carMapper.toEntity(carDto);
        carEntity.setDeleted(false);
        Driver driverEntity = driverRepository.findById(driverId).orElseThrow(() -> new DriverNotFoundException(
                String.format("Driver with id %s not found", driverId)
        ));
        driverEntity.setDeleted(false);
        carEntity.setDriver(driverEntity);

        driverRepository.save(driverEntity);
        carRepository.save(carEntity);

        return carMapper.toDto(carEntity);

    }

    @Override
    public CarDto updateCarByCarIdAndDriverId(Long carId, Long driverId, CarDto carDto) {

        Car carEntity = carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new CarNotFoundException(String.format("Car with id %s not found", carId)));
        carMapper.partialUpdate(carDto, carEntity);
        carEntity.setId(carId);
        Driver driverEntity = driverRepository.findById(driverId).orElseThrow(() -> new DriverNotFoundException(
                String.format("Driver with id %s not found", driverId)
        ));
        carEntity.setDriver(driverEntity);

        driverRepository.save(driverEntity);
        carRepository.save(carEntity);

        return carMapper.toDto(carEntity);
    }

    @Override
    public String safeDeleteCarByCarId(Long carId) {
        Car carEntity = carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new CarNotFoundException("Car with id " + carId + " not found"));
        carEntity.setDeleted(true);
        carRepository.save(carEntity);
        return String.format("Car with id %s soft deleted", carId);
    }

    @Override
    public DriverCarDto getDriverWithCars(Long driverId) {
        Driver driverEntity = driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(String.format("Driver with id %s not found", driverId)));

        return driverMapper.toDriverCarDto(driverEntity);
    }

}
