package com.modsen.driverservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.mapper.ListContainerMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private ListContainerMapper listContainerMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;


    private Driver driver;
    private DriverDto driverDto;

    @BeforeEach
    void setup() {
        driver = new Driver();
        driver.setId(1L);
        driver.setName("Jane Doe");
        driver.setEmail("janedoe@example.com");
        driver.setPhone("+1234567890");
        driver.setGender("Female");
        driver.setDeleted(false);
        driver.setAverageRating(4.5);

        // Создание экземпляра DriverDto
        driverDto = new DriverDto(
                driver.getId(),
                driver.getName(),
                driver.getEmail(),
                driver.getPhone(),
                driver.getGender(),
                driver.getAverageRating(),
                driver.getDeleted()
        );
    }

    @Test
    @DisplayName("Test createDriver(DriverDto); " +
            "then calls checkDriverRestoreOption and throws DuplicateDriverEmailPhoneException")
    void testCreateDriver_thenCallCheckDriverRestoreOptionAndThrowDuplicateDriverEmailPhoneException() {
        //Arrange
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        //Act and Assert
        assertThrows(DuplicateDriverEmailPhoneException.class, () -> driverServiceImpl.createDriver(driverDto));
        verify(driverRepository).existsByEmailAndDeletedIsTrue(anyString());
    }

    @Test
    @DisplayName("Test createDriver(DriverDto);" +
            " then calls checkCarExistsByPhoneOrEmail and throws DuplicateDriverEmailPhoneException")
    void testCreateDriver_thenCallCheckCarExistsByPhoneOrEmailAndThrowDuplicateDriverEmailPhoneException() {
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
            .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        //Act and Assert
        assertThrows(DuplicateDriverEmailPhoneException.class, () -> driverServiceImpl.createDriver(driverDto));
        verify(driverRepository).existsByPhoneAndDeletedIsFalse(anyString());
    }

    @Test
    @DisplayName("Test createDriver(DriverDto); then success")
    void testCreateDriver_thenSuccess() {
        //Arrange
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(false);
        when(driverMapper.toEntity(any(DriverDto.class))).thenReturn(driver);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(driverDto);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        //Act
        DriverDto driverDtoResponse = driverServiceImpl.createDriver(driverDto);

        //Assert
        assertNotNull(driverDtoResponse);
        assertSame(driverDto, driverDtoResponse);
        verify(driverRepository).existsByEmailAndDeletedIsTrue(anyString());
        verify(driverRepository).existsByPhoneAndDeletedIsFalse(anyString());

    }

    @Test
    @DisplayName("Test getPageDrivers returns a page of drivers; then success")
    void testGetPageDrivers_ReturnsPageOfDrivers() {
        // Arrange
        int offset = 0;
        int limit = 10;
        ListContainerResponseDto<DriverDto> expectedResponse = ListContainerResponseDto.<DriverDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(driverDto))
                .build();

        List<Driver> drivers = Collections.singletonList(driver);
        Page<Driver> driverPage = new PageImpl<>(drivers);

        when(driverRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit))).thenReturn(driverPage);
        when(driverMapper.toDto(driver)).thenReturn(driverDto);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<DriverDto> response = driverServiceImpl.getPageDrivers(offset, limit);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(driverDto, response.values().getFirst());
        verify(driverRepository).findAllByDeletedIsFalse(PageRequest.of(offset, limit));
        verify(driverMapper).toDto(any(Driver.class));
    }

    @Test
    @DisplayName("Test safeDeleteDriverByDriverId(Long) ; then calls getDriverByIdAndDeletedIsFalse" +
            " and throw DriverNotFoundException")
    void testSafeDeleteDriverByDriverId_thenCallGetDriverByIdAndDeletedIsFalseAndThrowDriverNotFoundException() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenThrow(DriverNotFoundException.class);

        //Act and assert
        assertThrows(DriverNotFoundException.class, () -> driverServiceImpl.safeDeleteDriverByDriverId(driverId));
    }

    @Test
    @DisplayName("Test safeDeleteDriverByDriverId(Long) ; then success")
    void testSafeDeleteDriverByDriverId_thenSuccess() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenReturn(Optional.ofNullable(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        //Act
        driverServiceImpl.safeDeleteDriverByDriverId(driverId);

        //Assert
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(driver);

    }

    @Test
    @DisplayName("Test updateDriverById ; then success")
    void testUpdateDriverById_thenSuccess() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(false);
        when(driverRepository.findByIdAndDeletedIsFalse(driverId)).thenReturn(Optional.ofNullable(driver));
        doNothing().when(driverMapper).partialUpdate(any(DriverDto.class), any(Driver.class));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(driverDto);

        //Act
        DriverDto driverDtoResponse = driverServiceImpl.updateDriverById(driverId, driverDto);
        assertNotNull(driverDtoResponse);
        assertSame(driverDto, driverDtoResponse);
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(driver);
    }

}
