package com.modsen.ratingservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.AppTestUtil;
import com.modsen.ratingservice.client.RideFeignClientException;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.exception.ApiExceptionDto;
import com.modsen.ratingservice.exception.rating.RatingNotFoundException;
import com.modsen.ratingservice.service.impl.DriverRatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(DriverRatingController.class)
class DriverRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverRatingService driverRatingService;

    @Test
    void createDriverRating_ReturnsDriverRatingDto_ValidRequest() throws Exception {
        // Arrange
        when(driverRatingService.createRating(any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(AppTestUtil.DRIVER_RATING_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.RATING_CREATE_REQUEST_DTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO)));
    }

    @Test
    void createDriverRating_ReturnsBadRequestStatusCode_BlankCommentField() throws Exception {
        // Arrange
        when(driverRatingService.createRating(any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(AppTestUtil.DRIVER_RATING_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.INVALID_RATING_CREATE_REQUEST_DTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateDriverRating_ReturnsUpdatedDriverRating_ValidRequest() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.updateRatingById(eq(ratingId), any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.RATING_UPDATE_REQUEST_DTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO)));
    }

    @Test
    void updateDriverRating_ReturnsBadRequestStatusCode_NullRideId() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.updateRatingById(eq(ratingId), any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.INVALID_RATING_UPDATE_REQUEST_DTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getDriverRating_ReturnsDriverRatingDto_ValidRequest() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.getRating(ratingId))
                .thenReturn(AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.RATING_RESPONSE_IN_SERVICE_DTO)));
    }

    @Test
    void getDriverRating_ReturnsNotFoundStatusCode_SuchRatingDoesntExists() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.getRating(ratingId))
                .thenThrow(new RatingNotFoundException(""));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteDriverRating_ReturnsNoContentStatusCode_ValidRequest() throws Exception {
        // Arrange
        doNothing().when(driverRatingService).safeDeleteRating(any(Long.class));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteDriverRating_ReturnsNotFoundStatusCode_SuchRatingDoesntExists() throws Exception {
        // Arrange
        doThrow(RatingNotFoundException.class)
                .when(driverRatingService).safeDeleteRating(any(Long.class));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void averageDriverRating_ReturnsAverageRatingDto_ValidRequest() throws Exception {
        // Arrange
        Long refUserId = 1L;
        AverageRatingResponseDto averageRatingResponseDto = new AverageRatingResponseDto(refUserId, 4.5);
        when(driverRatingService.getAverageRating(refUserId)).thenReturn(averageRatingResponseDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_AVERAGE_ENDPOINT, refUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        averageRatingResponseDto)));
    }

    @Test
    void averageDriverRating_ReturnNotFoundStatusCode_SuchDriverDoesntExists() throws Exception {
        // Arrange
        Long refUserId = 1L;
        when(driverRatingService.getAverageRating(refUserId))
                .thenThrow(new RideFeignClientException(new ApiExceptionDto(
                        HttpStatus.NOT_FOUND,
                        "",
                        LocalDateTime.now())));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_AVERAGE_ENDPOINT, refUserId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}