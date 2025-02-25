package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.TestData;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.mapper.CarMapper;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.CarRepository;
import com.modsen.driverservice.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarRepository carRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    @Test
    void createCar_CallCheckCarRestoreOptionAndThrowDuplicateCarNumbersException_SuchCarWasDeleted() {
        // Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(Mockito.<String>any()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.createCar(1L, TestData.CAR_DTO))
                .isInstanceOf(DuplicateCarNumbersException.class);
        verify(carRepository).existsByNumberAndDeletedIsTrue(TestData.CAR_DTO.number());
    }

    @Test
    void createCar_CallCheckCarExistsByNumberAndThrowDuplicateCarNumbersException_SuchCarAlreadyExists() {
        // Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.createCar(1L, TestData.CAR_DTO))
                .isInstanceOf(DuplicateCarNumbersException.class);
        verify(carRepository).existsByNumberAndDeletedIsFalse(TestData.CAR_DTO.number());
    }

    @Test
    void createCar_CarCreatedSuccessfully_ValidInputArgument() {
        // Arrange
        Long driverId = 1L;

        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(false);
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenReturn(Optional.of(TestData.DRIVER_ENTITY));

        when(carMapper.toEntity(TestData.CAR_DTO)).thenReturn(TestData.CAR_ENTITY);
        when(carMapper.toDto(TestData.CAR_ENTITY)).thenReturn(TestData.CAR_DTO);

        // Act
        CarDto actual = carServiceImpl.createCar(driverId, TestData.CAR_DTO);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(CarDto::number)
                .isEqualTo(TestData.CAR_DTO.number());
        verify(carRepository).save(TestData.CAR_ENTITY);
        verify(driverRepository).save(TestData.DRIVER_ENTITY);
    }

    @Test
    void safeDeleteCarByCarId_CarDeletedSuccessfully_ValidInputArgument() {
        // Arrange
        when(carRepository.findByIdAndDeletedIsFalse(any()))
                .thenReturn(Optional.of(TestData.CAR_ENTITY));
        when(carRepository.save(any(Car.class)))
                .thenReturn(TestData.CAR_ENTITY);

        // Act
        carServiceImpl.safeDeleteCarByCarId(1L);

        // Assert
        verify(carRepository).findByIdAndDeletedIsFalse(1L);
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void safeDeleteCarByCarId_CarNotFoundException_SuchCarDoesntExists() {
        //Arrange
        when(carRepository.findByIdAndDeletedIsFalse(any()))
                .thenThrow(CarNotFoundException.class);

        //Act and Assert
        assertThatThrownBy(() -> carServiceImpl.safeDeleteCarByCarId(1L))
                .isInstanceOf(CarNotFoundException.class);
        verify(carRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void getCarById_ReturnsCarDto_ValidInputArgument() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId))
                .thenReturn(Optional.of(TestData.CAR_ENTITY));
        when(carMapper.toDto(any(Car.class))).thenReturn(TestData.CAR_DTO);

        // Act
        CarDto actual = carServiceImpl.getCarById(carId);

        // Assert
        verify(carMapper).toDto(isA(Car.class));
        verify(carRepository).findByIdAndDeletedIsFalse(carId);
        assertThat(actual)
                .isSameAs(TestData.CAR_DTO);
    }

    @Test
    void getCarById_CarNotFoundException_SuchCarDoesntExists() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId)).thenThrow(CarNotFoundException.class);

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.getCarById(carId))
                .isInstanceOf(CarNotFoundException.class);
        verify(carRepository).findByIdAndDeletedIsFalse(carId);
    }

    @Test
    void updateCarByCarIdAndDriverId_CarUpdatedSuccessfully_ValidInputArgument() {
        //Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(false);
        when(carRepository.findByIdAndDeletedIsFalse(TestData.CAR_ENTITY.getId()))
                .thenReturn(Optional.of(TestData.CAR_ENTITY));
        when(driverRepository.findByIdAndDeletedIsFalse(TestData.DRIVER_ENTITY.getId()))
                .thenReturn(Optional.of(TestData.DRIVER_ENTITY));
        doNothing().when(carMapper).partialUpdate(any(CarDto.class), any(Car.class));
        when(carMapper.toDto(TestData.CAR_ENTITY)).thenReturn(TestData.CAR_DTO);
        when(driverRepository.save(any(Driver.class))).thenReturn(TestData.DRIVER_ENTITY);
        when(carRepository.save(any(Car.class))).thenReturn(TestData.CAR_ENTITY);

        // Act
        CarDto actual = carServiceImpl.updateCarByCarIdAndDriverId(TestData.CAR_ENTITY.getId(),
                TestData.DRIVER_ENTITY.getId(), TestData.CAR_DTO);

        // Assert
        verify(carRepository).findByIdAndDeletedIsFalse(TestData.CAR_ENTITY.getId());
        verify(carMapper).partialUpdate(TestData.CAR_DTO, TestData.CAR_ENTITY);
        verify(driverRepository).save(TestData.DRIVER_ENTITY);
        verify(carRepository).save(TestData.CAR_ENTITY);

        assertThat(actual)
                .isNotNull()
                .extracting(CarDto::id)
                .isEqualTo(TestData.CAR_ENTITY.getId());
    }

}
