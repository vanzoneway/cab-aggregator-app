package com.modsen.driverservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.modsen.driverservice.AppTestUtil;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.mapper.ListContainerMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;

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

    @Test
    @DisplayName("Test createDriver(DriverDto); " +
            "then calls checkDriverRestoreOption and throws DuplicateDriverEmailPhoneException")
    void testCreateDriver_thenCallCheckDriverRestoreOptionAndThrowDuplicateDriverEmailPhoneException() {
        //Arrange
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        //Act and Assert
        assertThatThrownBy(() -> driverServiceImpl.createDriver(AppTestUtil.driverDto))
                .isInstanceOf(DuplicateDriverEmailPhoneException.class);
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
                .thenReturn("");

        //Act and Assert
        assertThatThrownBy(() -> driverServiceImpl.createDriver(AppTestUtil.driverDto))
                .isInstanceOf(DuplicateDriverEmailPhoneException.class);
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
        when(driverMapper.toEntity(any(DriverDto.class))).thenReturn(AppTestUtil.driverEntity);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(AppTestUtil.driverDto);
        when(driverRepository.save(any(Driver.class))).thenReturn(AppTestUtil.driverEntity);

        //Act
        DriverDto actual = driverServiceImpl.createDriver(AppTestUtil.driverDto);

        //Assert
        assertThat(actual)
                .isNotNull()
                .isSameAs(AppTestUtil.driverDto);
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
                .withValues(Collections.singletonList(AppTestUtil.driverDto))
                .build();

        List<Driver> drivers = Collections.singletonList(AppTestUtil.driverEntity);
        Page<Driver> driverPage = new PageImpl<>(drivers);

        when(driverRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit))).thenReturn(driverPage);
        when(driverMapper.toDto(AppTestUtil.driverEntity)).thenReturn(AppTestUtil.driverDto);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<DriverDto> actual = driverServiceImpl.getPageDrivers(offset, limit);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(ListContainerResponseDto::totalElements, ListContainerResponseDto::totalPages,
                        r -> r.values().getFirst())
                .containsExactly(1L, 1, AppTestUtil.driverDto);
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
        assertThatThrownBy(() -> driverServiceImpl.safeDeleteDriverByDriverId(driverId))
                .isInstanceOf(DriverNotFoundException.class);
    }

    @Test
    @DisplayName("Test safeDeleteDriverByDriverId(Long) ; then success")
    void testSafeDeleteDriverByDriverId_thenSuccess() {
        //Arrange
        Long driverId = 1L;
        when(driverRepository.findByIdAndDeletedIsFalse(driverId))
                .thenReturn(Optional.of(AppTestUtil.driverEntity));
        when(driverRepository.save(any(Driver.class))).thenReturn(AppTestUtil.driverEntity);

        //Act
        driverServiceImpl.safeDeleteDriverByDriverId(driverId);

        //Assert
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(AppTestUtil.driverEntity);

    }

    @Test
    @DisplayName("Test updateDriverById; then success")
    void testUpdateDriverById_thenSuccess() {
        // Arrange
        Long driverId = 1L;
        when(driverRepository.existsByEmailAndDeletedIsTrue(anyString()))
                .thenReturn(false);
        when(driverRepository.existsByPhoneAndDeletedIsFalse(anyString()))
                .thenReturn(false);
        when(driverRepository.findByIdAndDeletedIsFalse(driverId)).thenReturn(Optional.of(AppTestUtil.driverEntity));
        doNothing().when(driverMapper).partialUpdate(any(DriverDto.class), any(Driver.class));
        when(driverRepository.save(any(Driver.class))).thenReturn(AppTestUtil.driverEntity);
        when(driverMapper.toDto(any(Driver.class))).thenReturn(AppTestUtil.driverDto);

        // Act
        DriverDto actual = driverServiceImpl.updateDriverById(driverId, AppTestUtil.driverDto);

        // Assert
        verify(driverRepository).findByIdAndDeletedIsFalse(driverId);
        verify(driverRepository).save(AppTestUtil.driverEntity);
        assertThat(actual)
                .isNotNull()
                .isEqualTo(AppTestUtil.driverDto);
    }

}
