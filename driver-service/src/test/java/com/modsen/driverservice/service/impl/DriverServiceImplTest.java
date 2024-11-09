package com.modsen.driverservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.modsen.driverservice.TestData;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.mapper.ListContainerMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;

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

    @Test
    void createDriver_CallCheckDriverRestoreOptionAndThrowDuplicateDriverEmailPhoneException_SuchDriverWasDeleted() {
        //Arrange
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        //Act and Assert
        assertThatThrownBy(() -> driverServiceImpl.createDriver(TestData.DRIVER_DTO))
                .isInstanceOf(DuplicateDriverEmailPhoneException.class);
        verify(driverRepository).existsByEmailAndDeletedIsTrue(anyString());
    }

    @Test
    void createDriver_CallCheckCarExistsByPhoneOrEmailAndThrowDuplicateDriverEmailPhoneException_SuchDriverExists() {
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        //Act and Assert
        assertThatThrownBy(() -> driverServiceImpl.createDriver(TestData.DRIVER_DTO))
                .isInstanceOf(DuplicateDriverEmailPhoneException.class);
        verify(driverRepository).existsByPhoneAndDeletedIsFalse(anyString());
    }

    @Test
    void createDriver_ReturnsCreatedDriverDto_ValidInputArgument() {
        //Arrange
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(false);
        when(driverMapper.toEntity(any(DriverDto.class))).thenReturn(TestData.DRIVER_ENTITY);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(TestData.DRIVER_DTO);
        when(driverRepository.save(any(Driver.class))).thenReturn(TestData.DRIVER_ENTITY);

        //Act
        DriverDto actual = driverServiceImpl.createDriver(TestData.DRIVER_DTO);

        //Assert
        assertThat(actual)
                .isNotNull()
                .isSameAs(TestData.DRIVER_DTO);
        verify(driverRepository).existsByEmailAndDeletedIsTrue(anyString());
        verify(driverRepository).existsByPhoneAndDeletedIsFalse(anyString());

    }

    @Test
    void getPageDrivers_ReturnsPageDriverDto_ValidInputArguments() {
        // Arrange
        int offset = 0;
        int limit = 10;
        ListContainerResponseDto<DriverDto> expectedResponse = ListContainerResponseDto.<DriverDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(TestData.DRIVER_DTO))
                .build();

        List<Driver> drivers = Collections.singletonList(TestData.DRIVER_ENTITY);
        Page<Driver> driverPage = new PageImpl<>(drivers);

        when(driverRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit))).thenReturn(driverPage);
        when(driverMapper.toDto(TestData.DRIVER_ENTITY)).thenReturn(TestData.DRIVER_DTO);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<DriverDto> actual = driverServiceImpl.getPageDrivers(offset, limit);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(ListContainerResponseDto::totalElements, ListContainerResponseDto::totalPages,
                        r -> r.values().getFirst())
                .containsExactly(1L, 1, TestData.DRIVER_DTO);
        verify(driverRepository).findAllByDeletedIsFalse(PageRequest.of(offset, limit));
        verify(driverMapper).toDto(any(Driver.class));
    }

    @Test
    void safeDeleteDriverByDriverId_CallGetDriverByIdAndDeletedIsFalseAndDriverNotFoundException_DriverDoesntExists() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenThrow(DriverNotFoundException.class);

        //Act and assert
        assertThatThrownBy(() -> driverServiceImpl.safeDeleteDriverByDriverId(driverId))
                .isInstanceOf(DriverNotFoundException.class);
    }

    @Test
    void safeDeleteDriverByDriverId_DriverDeletedSuccessfully_DriverExists() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenReturn(Optional.of(TestData.DRIVER_ENTITY));
        when(driverRepository.save(any(Driver.class))).thenReturn(TestData.DRIVER_ENTITY);

        //Act
        driverServiceImpl.safeDeleteDriverByDriverId(driverId);

        //Assert
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(TestData.DRIVER_ENTITY);

    }

    @Test
    void updateDriverById_ReturnsUpdatedDriverDto_ValidInputArguments() {
        // Arrange
        Long driverId = 1L;
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(false);
        when(driverRepository.findByIdAndDeletedIsFalse(driverId)).thenReturn(Optional.of(TestData.DRIVER_ENTITY));
        doNothing().when(driverMapper).partialUpdate(any(DriverDto.class), any(Driver.class));
        when(driverRepository.save(any(Driver.class))).thenReturn(TestData.DRIVER_ENTITY);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(TestData.DRIVER_DTO);

        // Act
        DriverDto actual = driverServiceImpl.updateDriverById(driverId, TestData.DRIVER_DTO);

        // Assert
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(TestData.DRIVER_ENTITY);
        assertThat(actual)
                .isNotNull()
                .isEqualTo(TestData.DRIVER_DTO);
    }

}
