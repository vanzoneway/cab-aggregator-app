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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final CarMapper carMapper;
    private final DriverMapper driverMapper;
    private final MessageSource messageSource;

    @Override
    public CarDto createCar(Long driverId, CarDto carDto) {
        if (carRepository.existsByNumberAndDeletedIsFalse(carDto.number())) {
            throw new DuplicateCarNumbersException(messageSource.getMessage(
                    "car.number.duplicate",
                    new Object[]{carDto.number()},
                    LocaleContextHolder.getLocale()));
        }
        Car carEntity = carMapper.toEntity(carDto);
        carEntity.setDeleted(false);
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        driverEntity.setDeleted(false);
        carEntity.setDriver(driverEntity);

        driverRepository.save(driverEntity);
        carRepository.save(carEntity);

        return carMapper.toDto(carEntity);
    }

    @Override
    public CarDto updateCarByCarIdAndDriverId(Long carId, Long driverId, CarDto carDto) {
        Car carEntity = getCarByIdAndDeletedIsFalse(carId);
        carMapper.partialUpdate(carDto, carEntity);
        carEntity.setId(carId);
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        carEntity.setDriver(driverEntity);

        driverRepository.save(driverEntity);
        carRepository.save(carEntity);

        return carMapper.toDto(carEntity);
    }

    @Override
    public void safeDeleteCarByCarId(Long carId) {
        Car carEntity = getCarByIdAndDeletedIsFalse(carId);
        carEntity.setDeleted(true);
        carRepository.save(carEntity);
    }

    @Override
    public DriverCarDto getDriverWithCars(Long driverId) {
        Driver driverEntity = driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        "driver.not.found",
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));

        return driverMapper.toDriverCarDto(driverEntity);
    }

    private Car getCarByIdAndDeletedIsFalse(Long carId) {
        return carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new CarNotFoundException(messageSource.getMessage(
                        "car.not.found",
                        new Object[]{carId},
                        LocaleContextHolder.getLocale())));
    }

    private Driver getDriverByIdAndDeletedIsFalse(Long driverId) {
        return driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        "driver.not.found",
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));
    }

}
