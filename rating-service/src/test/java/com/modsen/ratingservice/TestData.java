package com.modsen.ratingservice;

import com.modsen.ratingservice.client.RideResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.model.DriverRating;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestData {

    private TestData() {}

    public static final String DRIVER_RATING_ENDPOINT = "/api/v1/drivers-ratings";
    public static final String DRIVER_RATING_UPDATE_DELETE_ENDPOINT = "/api/v1/drivers-ratings/{id}";
    public static final String DRIVER_RATING_AVERAGE_ENDPOINT = "/api/v1/drivers-ratings/{refUserId}/average";

    public static final RatingResponseDto RATING_RESPONSE_DTO = new RatingResponseDto(
            1L,
            "Great ride!",
            "DRIVER",
            1L,
            5,
            1001L);

    public static final RatingRequestDto RATING_UPDATE_REQUEST_DTO = new RatingRequestDto(
            "Great ride!",
            5,
            null);

    public static final RatingRequestDto INVALID_RATING_UPDATE_REQUEST_DTO = new RatingRequestDto(
            "Great ride!",
            10,
            null);

    public static final RatingRequestDto RATING_CREATE_REQUEST_DTO = new RatingRequestDto(
            "Great ride!",
            5,
            1001L);
    public static final RatingRequestDto INVALID_RATING_CREATE_REQUEST_DTO = new RatingRequestDto(
            "",
            null,
            null);

    public static final RatingRequestDto RATING_REQUEST_DTO = new RatingRequestDto(
            "Great experience!", 5, 1L);

    public static final AverageRatingResponseDto AVERAGE_RATING_RESPONSE_DTO = new AverageRatingResponseDto(
            1L, 4.5);

    public static final RatingResponseDto RATING_RESPONSE_IN_SERVICE_DTO = new RatingResponseDto(
            1L,
            "Excellent ride!",
            "DRIVER",
            1L,
            5,
            1L
    );

    public static final RideResponseDto  RIDE_RESPONSE_DTO = new RideResponseDto(
            1L,
            101L,
            202L,
            "123 Main St",
            "456 Elm St",
            "COMPLETED",
            LocalDateTime.parse("2023-10-01T10:15:30"),
            BigDecimal.valueOf(15.75)
    );

    public static final DriverRating DRIVER_RATING;

    static {

        DRIVER_RATING = new DriverRating();
        DRIVER_RATING.setComment("Excellent ride!");
        DRIVER_RATING.setRating(3);
        DRIVER_RATING.setRideId(1L);
        DRIVER_RATING.setRefUserId(1L);
        DRIVER_RATING.setDeleted(false);

    }

}
