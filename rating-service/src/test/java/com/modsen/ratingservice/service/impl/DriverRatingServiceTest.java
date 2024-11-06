package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.AppTestUtil;
import com.modsen.ratingservice.client.RideFeignClient;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.kafka.KafkaProducerSender;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.impl.DriverRatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import org.junit.jupiter.api.DisplayName;
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
    private KafkaProducerSender kafkaProducerSender;

    @InjectMocks
    private DriverRatingService driverRatingService;


    @Test
    @DisplayName("Test createRating(RatingRequestDto); then success")
    void testCreateRating_thenSuccess() {
        when(rideFeignClient.findRideById(anyLong(), anyString()))
                .thenReturn(AppTestUtil.rideResponseDto);
        when(repository.existsByRideIdAndDeletedIsFalse(anyLong()))
                .thenReturn(false);
        when(repository.existsByRideIdAndDeletedIsTrue(anyLong()))
                .thenReturn(false);
        when(ratingMapper.toRating(any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.driverRating);
        when(repository.save(any(DriverRating.class)))
                .thenReturn(AppTestUtil.driverRating);
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act
        RatingResponseDto actual = driverRatingService.createRating(AppTestUtil.ratingRequestDto);

        // Assert
        assertThat(actual).isEqualTo(AppTestUtil.ratingResponseInServiceDto);
        verify(repository).save(any(DriverRating.class));
        verify(repository).existsByRideIdAndDeletedIsFalse(anyLong());
        verify(repository).existsByRideIdAndDeletedIsTrue(anyLong());
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test updateRatingById(Long, RatingRequestDto); then success")
    void testUpdateRatingById_thenSuccess() {
        // Arrange
        when(repository.existsByRideIdAndDeletedIsFalse(anyLong()))
                .thenReturn(false);
        when(repository.existsByRideIdAndDeletedIsTrue(anyLong()))
                .thenReturn(false);
        when(repository.findByIdAndDeletedIsFalse(anyLong()))
                .thenReturn(Optional.of(AppTestUtil.driverRating));
        doNothing().when(ratingMapper).partialUpdate(any(RatingRequestDto.class), any(DriverRating.class));
        when(repository.save(any(DriverRating.class)))
                .thenReturn(AppTestUtil.driverRating);
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act
        RatingResponseDto result = driverRatingService.updateRatingById(1L, AppTestUtil.ratingRequestDto);

        // Assert
        assertThat(result).isEqualTo(AppTestUtil.ratingResponseInServiceDto);
        verify(repository).existsByRideIdAndDeletedIsFalse(anyLong());
        verify(repository).existsByRideIdAndDeletedIsTrue(anyLong());
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test getRating(Long); then success")
    void testGetRating_thenSuccess() {
        // Arrange
        when(repository.findByIdAndDeletedIsFalse(anyLong())).thenReturn(Optional.of(AppTestUtil.driverRating));
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act
        RatingResponseDto actual = driverRatingService.getRating(1L);

        // Assert
        assertThat(actual).isEqualTo(AppTestUtil.ratingResponseInServiceDto);
        verify(repository).findByIdAndDeletedIsFalse(anyLong());
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test getRatingsByRefUserId(Long, Integer, Integer); then success")
    void testGetRatingsByRefUserId_thenSuccess() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RatingResponseDto> expectedResponse = ListContainerResponseDto
                .<RatingResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.ratingResponseInServiceDto))
                .build();
        List<DriverRating> ratings = Collections.singletonList(AppTestUtil.driverRating);
        Page<DriverRating> passengerPage = new PageImpl<>(ratings);
        when(repository.findAllByRefUserIdAndDeletedIsFalse(anyLong(), any(Pageable.class)))
                .thenReturn(passengerPage);
        when(ratingMapper.toDto(any(DriverRating.class), anyString()))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);
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
    @DisplayName("Test safeDeleteRating(Long); then success")
    void testSafeDeleteRating_thenSuccess() {
        // Arrange
        when(repository.findByIdAndDeletedIsFalse(anyLong()))
                .thenReturn(Optional.of(AppTestUtil.driverRating));
        when(repository.save(any(DriverRating.class)))
                .thenReturn(AppTestUtil.driverRating);

        // Act
        driverRatingService.safeDeleteRating(1L);

        // Assert
        verify(repository).findByIdAndDeletedIsFalse(anyLong());
        verify(repository).save(any(DriverRating.class));
    }

    @Test
    @DisplayName("Test getAverageRating(Long); then success")
    void testGetAverageRating_thenSuccess() {
        // Arrange
        when(repository.getAverageRatingByRefUserId(anyLong()))
                .thenReturn(Optional.of(4.5));
        doNothing().when(kafkaProducerSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));

        // Act
        AverageRatingResponseDto result = driverRatingService.getAverageRating(1L);

        // Assert
        assertThat(result.averageRating()).isEqualTo(AppTestUtil.averageRatingResponseDto.averageRating());
        verify(repository).getAverageRatingByRefUserId(anyLong());
        verify(kafkaProducerSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));
    }

}
