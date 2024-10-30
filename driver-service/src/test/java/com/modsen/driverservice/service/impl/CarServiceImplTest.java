package com.modsen.driverservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.mapper.CarMapper;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.CarRepository;
import com.modsen.driverservice.repository.DriverRepository;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

    private CarDto carDto;
    private Car carEntity;
    private Driver driverEntity;

    @BeforeEach
    void setup() {
        Long anyId = 1L;
        carDto = new CarDto(anyId, "Brand", "Color", "42", "Model", 1, true);

        driverEntity = new Driver();
        driverEntity.setAverageRating(10.0d);
        driverEntity.setCars(new ArrayList<>());
        driverEntity.setDeleted(true);
        driverEntity.setEmail("jane.doe@example.org");
        driverEntity.setGender("Gender");
        driverEntity.setId(anyId);
        driverEntity.setName("Name");
        driverEntity.setPhone("6625550144");

        carEntity = new Car();
        carEntity.setBrand("Brand");
        carEntity.setColor("Color");
        carEntity.setDeleted(true);
        carEntity.setDriver(driverEntity);
        carEntity.setId(anyId);
        carEntity.setModel("Model");
        carEntity.setNumber("42");
        carEntity.setYear(1);

    }

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
        assertThrows(DuplicateCarNumbersException.class,
                () -> carServiceImpl.createCar(1L, carDto));
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
        assertThrows(DuplicateCarNumbersException.class,
                () -> carServiceImpl.createCar(1L, carDto));
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
                .thenReturn(Optional.of(driverEntity));

        when(carMapper.toEntity(carDto)).thenReturn(carEntity);
        when(carMapper.toDto(carEntity)).thenReturn(carDto);

        // Act
        CarDto result = carServiceImpl.createCar(driverId, carDto);

        // Assert
        assertNotNull(result);
        assertEquals(carDto.number(), result.number());
        verify(carRepository).save(carEntity);
        verify(driverRepository).save(driverEntity);
    }

    @Test
    @DisplayName("Test safeDeleteCarByCarId(Long); then calls findByIdAndDeletedIsFalse(Long) and success")
    void testSafeDeleteCarByCarId_success() {
        // Arrange
        when(carRepository.findByIdAndDeletedIsFalse(any()))
                .thenReturn(Optional.of(carEntity));
        when(carRepository.save(any(Car.class)))
                .thenReturn(carEntity);

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

        //Assert
        assertThrows(CarNotFoundException.class, () -> carServiceImpl.safeDeleteCarByCarId(1L));
        verify(carRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    @DisplayName("Test getCarById(Long); then success")
    void testGetCarById() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId))
                .thenReturn(Optional.of(carEntity));
        when(carMapper.toDto(any(Car.class))).thenReturn(carDto);

        // Act
        CarDto actualCarById = carServiceImpl.getCarById(carId);

        // Assert
        verify(carMapper).toDto(isA(Car.class));
        verify(carRepository).findByIdAndDeletedIsFalse(carId);
        assertSame(carDto, actualCarById);
    }

    @Test
    @DisplayName("Test getCarById(Long); then throw DuplicateCarNumbersException")
    void testGetCarById_thenThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findByIdAndDeletedIsFalse(carId)).thenThrow(CarNotFoundException.class);

        // Act and Assert
        assertThrows(CarNotFoundException.class, () -> carServiceImpl.getCarById(1L));
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
        when(carRepository.findByIdAndDeletedIsFalse(carEntity.getId()))
                .thenReturn(Optional.of(carEntity));
        when(driverRepository.findByIdAndDeletedIsFalse(driverEntity.getId()))
                .thenReturn(Optional.of(driverEntity));
        doNothing().when(carMapper).partialUpdate(any(CarDto.class), any(Car.class));
        when(carMapper.toDto(carEntity)).thenReturn(carDto);
        when(driverRepository.save(any(Driver.class))).thenReturn(driverEntity);
        when(carRepository.save(any(Car.class))).thenReturn(carEntity);

        // Act
        CarDto result = carServiceImpl.updateCarByCarIdAndDriverId(carEntity.getId(), driverEntity.getId(), carDto);

        // Assert
        verify(carRepository).findByIdAndDeletedIsFalse(carEntity.getId());
        verify(carMapper).partialUpdate(carDto, carEntity);
        verify(driverRepository).save(driverEntity);
        verify(carRepository).save(carEntity);

        assertNotNull(result);
        assertEquals(carEntity.getId(), carDto.id());

    }

}
