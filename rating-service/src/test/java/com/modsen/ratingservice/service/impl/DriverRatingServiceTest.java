package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.client.RideFeignClient;
import com.modsen.ratingservice.client.RideResponseDto;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.kafka.KafkaProducerSender;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.impl.DriverRatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
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
    private MessageSource messageSource;

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private KafkaProducerSender kafkaProducerSender;

    @InjectMocks
    private DriverRatingService driverRatingService;

    private DriverRating driverRating;

    private RatingRequestDto ratingRequestDto;
    private AverageRatingResponseDto averageRatingResponseDto;
    private RatingResponseDto ratingResponseDto;
    private RideResponseDto rideResponseDto;

    @BeforeEach
    void setup() {
        driverRating = new DriverRating();
        driverRating.setComment("Excellent ride!");
        driverRating.setRating(3);
        driverRating.setRideId(1L);
        driverRating.setRefUserId(1L);
        driverRating.setDeleted(false);

        ratingRequestDto = new RatingRequestDto("Great experience!", 5, 1L);

        averageRatingResponseDto = new AverageRatingResponseDto(1L, 4.5);

        ratingResponseDto = new RatingResponseDto(
                1L,
                "Excellent ride!",
                "DRIVER",
                1,
                5,
                1L
        );
        rideResponseDto = new RideResponseDto(
                1L,
                101L,
                202L,
                "123 Main St",
                "456 Elm St",
                "COMPLETED",
                LocalDateTime.parse("2023-10-01T10:15:30"),
                BigDecimal.valueOf(15.75)
        );
    }

    @Test
    @DisplayName("Test createRating(RatingRequestDto); then success")
    void testCreateRating_thenSuccess() {
        when(rideFeignClient.findRideById(any(Long.class), any(String.class)))
                .thenReturn(rideResponseDto);
        when(repository.existsByRideIdAndDeletedIsFalse(any(Long.class))).thenReturn(false);
        when(repository.existsByRideIdAndDeletedIsTrue(any(Long.class))).thenReturn(false);
        when(ratingMapper.toRating(any(RatingRequestDto.class))).thenReturn(driverRating);
        when(repository.save(any(DriverRating.class))).thenReturn(driverRating);
        when(ratingMapper.toDto(any(DriverRating.class), anyString())).thenReturn(ratingResponseDto);

        //Act
        RatingResponseDto result = driverRatingService.createRating(ratingRequestDto);

        //Assert
        assertSame(result, ratingResponseDto);
        verify(repository).save(any(DriverRating.class));
        verify(repository).existsByRideIdAndDeletedIsFalse(any(Long.class));
        verify(repository).existsByRideIdAndDeletedIsTrue(any(Long.class));
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test updateRatingById(Long, RatingRequestDto); then success")
    void testUpdateRatingById_thenSuccess() {
        //Arrange
        when(repository.existsByRideIdAndDeletedIsFalse(any(Long.class))).thenReturn(false);
        when(repository.existsByRideIdAndDeletedIsTrue(any(Long.class))).thenReturn(false);
        when(repository.findByIdAndDeletedIsFalse(any(Long.class))).thenReturn(Optional.ofNullable(driverRating));
        doNothing().when(ratingMapper).partialUpdate(any(RatingRequestDto.class), any(DriverRating.class));
        when(repository.save(any(DriverRating.class))).thenReturn(driverRating);
        when(ratingMapper.toDto(any(DriverRating.class), anyString())).thenReturn(ratingResponseDto);

        //Act
        RatingResponseDto result = driverRatingService.updateRatingById(1L, ratingRequestDto);

        //Assert
        assertSame(result, ratingResponseDto);
        verify(repository).existsByRideIdAndDeletedIsFalse(any(Long.class));
        verify(repository).existsByRideIdAndDeletedIsTrue(any(Long.class));
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());

    }

    @Test
    @DisplayName("Test getRating(Long); then success")
    void testGetRating_thenSuccess() {
        //Arrange
        when(repository.findByIdAndDeletedIsFalse(any(Long.class))).thenReturn(Optional.ofNullable(driverRating));
        when(ratingMapper.toDto(any(DriverRating.class), anyString())).thenReturn(ratingResponseDto);

        //Act
        RatingResponseDto result = driverRatingService.getRating(1L);

        //Assert
        assertSame(result, ratingResponseDto);
        verify(repository).findByIdAndDeletedIsFalse(any(Long.class));
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test getRatingsByRefUserId(Long, Integer, Integer); then success")
    void testGetRatingsByRefUserId_thenSuccess() {
        //Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RatingResponseDto> expectedResponse = ListContainerResponseDto
                .<RatingResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(ratingResponseDto))
                .build();
        List<DriverRating> ratings = Collections.singletonList(driverRating);
        Page<DriverRating> passengerPage = new PageImpl<>(ratings);
        when(repository.findAllByRefUserIdAndDeletedIsFalse(any(Long.class), any(Pageable.class))).thenReturn(passengerPage);
        when(ratingMapper.toDto(any(DriverRating.class), anyString())).thenReturn(ratingResponseDto);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);

        //Act
        ListContainerResponseDto<RatingResponseDto> result = driverRatingService
                .getRatingsByRefUserId(1L, offset, limit);

        //Assert
        assertSame(result, expectedResponse);
        verify(repository).findAllByRefUserIdAndDeletedIsFalse(any(Long.class), any(Pageable.class));
        verify(ratingMapper).toDto(any(DriverRating.class), anyString());
    }

    @Test
    @DisplayName("Test safeDeleteRating(Long); then success")
    void testSafeDeleteRating_thenSuccess() {
        //Arrange
        when(repository.findByIdAndDeletedIsFalse(any(Long.class))).thenReturn(Optional.ofNullable(driverRating));
        when(repository.save(any(DriverRating.class))).thenReturn(driverRating);

        //Act
        driverRatingService.safeDeleteRating(1L);

        //Assert
        verify(repository).findByIdAndDeletedIsFalse(any(Long.class));
        verify(repository).save(any(DriverRating.class));
    }

    @Test
    @DisplayName("Test getAverageRating(Long); then success")
    void testGetAverageRating_thenSuccess() {
        //Arrange
        when(repository.getAverageRatingByRefUserId(any(Long.class))).thenReturn(Optional.of(4.5));
        doNothing().when(kafkaProducerSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));

        //Act
        AverageRatingResponseDto result = driverRatingService.getAverageRating(1L);

        //Assert
        assertEquals(result.averageRating(), averageRatingResponseDto.averageRating());
        verify(repository).getAverageRatingByRefUserId(any(Long.class));
        verify(kafkaProducerSender).sendAverageRatingToDriver(any(AverageRatingResponseDto.class));

    }


}
