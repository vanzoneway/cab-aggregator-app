package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.TestData;
import com.modsen.ratingservice.client.RideFeignClient;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.kafka.producer.AverageRatingSender;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.impl.DriverRatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverRatingServiceTest {

    @Mock
    private DriverRatingRepository repository;

    @Mock
    private DriverRatingMapper ratingMapper;

    @Mock
    private ListContainerMapper listContainerMapper;

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private AverageRatingSender averageRatingSender;

    @InjectMocks
    private DriverRatingService driverRatingService;

    @Test
    void updateRatingById_ReturnsUpdatedRatingDto_ValidInputArguments() {
        // Arrange
        when(repository.existsByRideIdAndDeletedIsFalse(anyLong()))
                .thenReturn(false);
        when(repository.existsByRideIdAndDeletedIsTrue(anyLong()))
                .thenReturn(false);
        when(repository.findByIdAndDeletedIsFalse(anyLong()))
                .thenReturn(Optional.of(TestData.DRIVER_RATING));
        doNothing().when(ratingMapper).partialUpdate(any(RatingRequestDto.class), any(DriverRating.class));
        when(repository.save(any(DriverRating.class)))
                .thenReturn(TestData.DRIVER_RATING);
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(TestData.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act
        RatingResponseDto result = driverRatingService.updateRatingById(1L, TestData.RATING_REQUEST_DTO);

        // Assert
        assertThat(result).isEqualTo(TestData.RATING_RESPONSE_IN_SERVICE_DTO);
        verify(repository).existsByRideIdAndDeletedIsFalse(anyLong());
        verify(repository).existsByRideIdAndDeletedIsTrue(anyLong());
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    void getRating_ReturnsRatingDto_ValidInputArguments() {
        // Arrange
        when(repository.findByIdAndDeletedIsFalse(anyLong())).thenReturn(Optional.of(TestData.DRIVER_RATING));
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(TestData.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act
        RatingResponseDto actual = driverRatingService.getRating(1L);

        // Assert
        assertThat(actual).isEqualTo(TestData.RATING_RESPONSE_IN_SERVICE_DTO);
        verify(repository).findByIdAndDeletedIsFalse(anyLong());
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    void getRatingsByRefUserId_ReturnsPageRatingDto_ValidInputArguments() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RatingResponseDto> expectedResponse = ListContainerResponseDto
                .<RatingResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(TestData.RATING_RESPONSE_IN_SERVICE_DTO))
                .build();
        List<DriverRating> ratings = Collections.singletonList(TestData.DRIVER_RATING);
        Page<DriverRating> passengerPage = new PageImpl<>(ratings);
        when(repository.findAllByRefUserIdAndDeletedIsFalse(anyLong(), any(Pageable.class)))
                .thenReturn(passengerPage);
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(TestData.RATING_RESPONSE_IN_SERVICE_DTO);
        when(listContainerMapper.toDto(any(Page.class)))
                .thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<RatingResponseDto> result = driverRatingService
                .getRatingsByRefUserId(1L, offset, limit);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(repository).findAllByRefUserIdAndDeletedIsFalse(anyLong(), any(Pageable.class));
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    void safeDeleteRating_RatingDeletedSuccessfully_ValidInputArguments() {
        // Arrange
        when(repository.findByIdAndDeletedIsFalse(anyLong()))
                .thenReturn(Optional.of(TestData.DRIVER_RATING));
        when(repository.save(any(DriverRating.class)))
                .thenReturn(TestData.DRIVER_RATING);

        // Act
        driverRatingService.safeDeleteRating(1L);

        // Assert
        verify(repository).findByIdAndDeletedIsFalse(anyLong());
        verify(repository).save(any(DriverRating.class));
    }

    @Test
    void getAverageRating_ReturnsAverageRatingDto_ValidInputArguments() {
        // Arrange
        when(repository.getAverageRatingByRefUserId(anyLong()))
                .thenReturn(Optional.of(4.5));
        doNothing().when(averageRatingSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));

        // Act
        AverageRatingResponseDto result = driverRatingService.getAverageRating(1L);

        // Assert
        assertThat(result.averageRating()).isEqualTo(TestData.AVERAGE_RATING_RESPONSE_DTO.averageRating());
        verify(repository).getAverageRatingByRefUserId(anyLong());
        verify(averageRatingSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));
    }

}
