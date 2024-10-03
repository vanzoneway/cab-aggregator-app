package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.mapper.CarMapper;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.CarRepository;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final CarMapper carMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public CarDto createCar(Long driverId, CarDto carDto) {
        checkCarRestoreOption(carDto);
        checkCarExistsByNumber(carDto);
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
    @Transactional
    public CarDto updateCarByCarIdAndDriverId(Long carId, Long driverId, CarDto carDto) {
        checkCarRestoreOption(carDto);
        checkCarExistsByNumber(carDto);
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
    @Transactional
    public void safeDeleteCarByCarId(Long carId) {
        Car carEntity = getCarByIdAndDeletedIsFalse(carId);
        carEntity.setDeleted(true);
        carRepository.save(carEntity);
    }

    @Override
    public CarDto getCarById(Long carId) {
        return carMapper.toDto(getCarByIdAndDeletedIsFalse(carId));
    }

    private void checkCarRestoreOption(CarDto carDto) {
        if (Objects.nonNull(carDto.number()) && carRepository.existsByNumberAndDeletedIsTrue(carDto.number())) {
            throw new DuplicateCarNumbersException(messageSource.getMessage(
                    AppConstants.RESTORE_CAR_MESSAGE_KEY,
                    new Object[]{carDto.number()},
                    LocaleContextHolder.getLocale()));
        }
    }

    private void checkCarExistsByNumber(CarDto carDto) {
        if (carRepository.existsByNumberAndDeletedIsFalse(carDto.number())) {
            throw new DuplicateCarNumbersException(messageSource.getMessage(
                    AppConstants.CAR_NUMBER_DUPLICATE_MESSAGE_KEY,
                    new Object[]{carDto.number()},
                    LocaleContextHolder.getLocale()));
        }
    }

    private Car getCarByIdAndDeletedIsFalse(Long carId) {
        return carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new CarNotFoundException(messageSource.getMessage(
                        AppConstants.CAR_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{carId},
                        LocaleContextHolder.getLocale())));
    }

    private Driver getDriverByIdAndDeletedIsFalse(Long driverId) {
        return driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        AppConstants.DRIVER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));
    }

}
