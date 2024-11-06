package com.modsen.driverservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.modsen.driverservice.AppTestUtil;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.mapper.CarMapper;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.CarRepository;
import com.modsen.driverservice.repository.DriverRepository;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

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
    @DisplayName("Test createCar(Long, CarDto); " +
            "then calls checkCarRestoreOption(carDto) and throws DuplicateCarNumbersException")
    void testCreateCar_thenCallCheckCarRestoreOptionAndThrowDuplicateCarNumbersException() {
        // Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(Mockito.<String>any()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.createCar(1L, AppTestUtil.carDto))
                .isInstanceOf(DuplicateCarNumbersException.class);
        verify(carRepository).existsByNumberAndDeletedIsTrue("42");
    }

    @Test
    @DisplayName("Test createCar(Long, CarDto); " +
            "then calls checkCarExistsByNumber(carDto) and throws DuplicateCarNumbersException")
    void testCreateCar_thenCallCheckCarExistsByNumberAndThrowDuplicateCarNumbersException() {
        // Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.createCar(1L, AppTestUtil.carDto))
                .isInstanceOf(DuplicateCarNumbersException.class);
        verify(carRepository).existsByNumberAndDeletedIsFalse("42");
    }

    @Test
    @DisplayName("Test createCar(Long, CarDto); " +
            "then creates a car successfully and returns the CarDto")
    void testCreateCar_success() {
        // Arrange
        Long driverId = 1L;

        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(false);
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenReturn(Optional.of(AppTestUtil.driverEntity));

        when(carMapper.toEntity(AppTestUtil.carDto)).thenReturn(AppTestUtil.carEntity);
        when(carMapper.toDto(AppTestUtil.carEntity)).thenReturn(AppTestUtil.carDto);

        // Act
        CarDto actual = carServiceImpl.createCar(driverId, AppTestUtil.carDto);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(CarDto::number)
                .isEqualTo(AppTestUtil.carDto.number());
        verify(carRepository).save(AppTestUtil.carEntity);
        verify(driverRepository).save(AppTestUtil.driverEntity);
    }

    @Test
    @DisplayName("Test safeDeleteCarByCarId(Long); then calls findByIdAndDeletedIsFalse(Long) and success")
    void testSafeDeleteCarByCarId_success() {
        // Arrange
        when(carRepository.findByIdAndDeletedIsFalse(any()))
                .thenReturn(Optional.of(AppTestUtil.carEntity));
        when(carRepository.save(any(Car.class)))
                .thenReturn(AppTestUtil.carEntity);

        // Act
        carServiceImpl.safeDeleteCarByCarId(1L);

        // Assert
        verify(carRepository).findByIdAndDeletedIsFalse(1L);
        verify(carRepository).save(any(Car.class));
    }

    @Test
    @DisplayName("Test safeDeleteCarByCarIId(Long); then calls findByIdAndDeletedIsFalse(Long) and CarNotFoundException")
    void testSafeDeleteCarByCarId_throwsCarNotFoundException() {
        //Arrange
        when(carRepository.findByIdAndDeletedIsFalse(any()))
                .thenThrow(CarNotFoundException.class);

        //Act and Assert
        assertThatThrownBy(() -> carServiceImpl.safeDeleteCarByCarId(1L))
                .isInstanceOf(CarNotFoundException.class);
        verify(carRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    @DisplayName("Test getCarById(Long); then success")
    void testGetCarById() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId))
                .thenReturn(Optional.of(AppTestUtil.carEntity));
        when(carMapper.toDto(any(Car.class))).thenReturn(AppTestUtil.carDto);

        // Act
        CarDto actual = carServiceImpl.getCarById(carId);

        // Assert
        verify(carMapper).toDto(isA(Car.class));
        verify(carRepository).findByIdAndDeletedIsFalse(carId);
        assertThat(actual)
                .isSameAs(AppTestUtil.carDto);
    }

    @Test
    @DisplayName("Test getCarById(Long); then throw DuplicateCarNumbersException")
    void testGetCarById_thenThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId)).thenThrow(CarNotFoundException.class);

        // Act and Assert
        assertThatThrownBy(() -> carServiceImpl.getCarById(carId))
                .isInstanceOf(CarNotFoundException.class);
        verify(carRepository).findByIdAndDeletedIsFalse(carId);
    }

    @Test
    @DisplayName("Test updateCarByCarIdAndDriverId(Long, Long, CarDto); then success")
    void testUpdateCarByCarIdAndDriverId_success() {
        //Arrange
        when(carRepository.existsByNumberAndDeletedIsTrue(any()))
                .thenReturn(false);
        when(carRepository.existsByNumberAndDeletedIsFalse(any()))
                .thenReturn(false);
        when(carRepository.findByIdAndDeletedIsFalse(AppTestUtil.carEntity.getId()))
                .thenReturn(Optional.of(AppTestUtil.carEntity));
        when(driverRepository.findByIdAndDeletedIsFalse(AppTestUtil.driverEntity.getId()))
                .thenReturn(Optional.of(AppTestUtil.driverEntity));
        doNothing().when(carMapper).partialUpdate(any(CarDto.class), any(Car.class));
        when(carMapper.toDto(AppTestUtil.carEntity)).thenReturn(AppTestUtil.carDto);
        when(driverRepository.save(any(Driver.class))).thenReturn(AppTestUtil.driverEntity);
        when(carRepository.save(any(Car.class))).thenReturn(AppTestUtil.carEntity);

        // Act
        CarDto actual = carServiceImpl.updateCarByCarIdAndDriverId(AppTestUtil.carEntity.getId(),
                AppTestUtil.driverEntity.getId(), AppTestUtil.carDto);

        // Assert
        verify(carRepository).findByIdAndDeletedIsFalse(AppTestUtil.carEntity.getId());
        verify(carMapper).partialUpdate(AppTestUtil.carDto, AppTestUtil.carEntity);
        verify(driverRepository).save(AppTestUtil.driverEntity);
        verify(carRepository).save(AppTestUtil.carEntity);

        assertThat(actual)
                .isNotNull()
                .extracting(CarDto::id)
                .isEqualTo(AppTestUtil.carEntity.getId());
    }

}
