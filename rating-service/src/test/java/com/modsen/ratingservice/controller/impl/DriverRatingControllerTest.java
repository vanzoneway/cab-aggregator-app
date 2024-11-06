package com.modsen.ratingservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.AppTestUtil;
import com.modsen.ratingservice.client.RideFeignClientException;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.exception.ApiExceptionDto;
import com.modsen.ratingservice.exception.rating.RatingNotFoundException;
import com.modsen.ratingservice.service.impl.DriverRatingService;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Test createDriverRating(RatingRequestDto); then success")
    void testCreateDriverRating_thenSuccess() throws Exception {
        // Arrange
        when(driverRatingService.createRating(any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(AppTestUtil.DRIVER_RATING_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.ratingCreateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.ratingResponseInServiceDto)));
    }

    @Test
    @DisplayName("Test createDriverRating(RatingRequestDto); then returns BAD_REQUEST status code")
    void testCreateDriverRating_shouldReturnBadRequestStatusCode_withInvalidParameters() throws Exception {
        // Arrange
        when(driverRatingService.createRating(any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(AppTestUtil.DRIVER_RATING_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.invalidRatingCreateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test updateDriverRating(Long, RatingRequestDto); then success")
    void testUpdateDriverRating_thenSuccess() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.updateRatingById(eq(ratingId), any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.ratingUpdateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.ratingResponseInServiceDto)));
    }

    @Test
    @DisplayName("Test updateDriverRating(Long, RatingRequestDto); then returns BAD_REQUEST status code")
    void testUpdateDriverRating_shouldReturnBadRequestStatusCode_withInvalidParameters() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.updateRatingById(eq(ratingId), any(RatingRequestDto.class)))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AppTestUtil.invalidRatingUpdateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test getDriverRating(Long); then success")
    void testGetDriverRating_thenSuccess() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.getRating(ratingId))
                .thenReturn(AppTestUtil.ratingResponseInServiceDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.ratingResponseInServiceDto)));
    }

    @Test
    @DisplayName("Test getDriverRating(Long); then returns NOT_FOUND status code")
    void testGetDriverRating_shouldReturnNotFoundStatusCode_whenSuchRatingDoesntExists() throws Exception {
        // Arrange
        Long ratingId = 1L;
        when(driverRatingService.getRating(ratingId))
                .thenThrow(new RatingNotFoundException(""));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, ratingId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test deleteDriverRating(Long); then success")
    void testDeleteDriverRating_thenSuccess() throws Exception {
        // Arrange
        doNothing().when(driverRatingService).safeDeleteRating(any(Long.class));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test deleteDriverRating(Long); then returns NOT_FOUND status code")
    void testDeleteDriverRating_shouldReturnNotFoundStatusCode_whenSuchRatingDoesntExists() throws Exception {
        // Arrange
        doThrow(new RatingNotFoundException("")).when(driverRatingService).safeDeleteRating(any(Long.class));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test averageDriverRating(Long); then success")
    void testAverageDriverRating_thenSuccess() throws Exception {
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
    @DisplayName("Test averageDriverRating(Long); then success")
    void testAverageDriverRating_shouldReturnNotFoundStatusCode_whenSuchDriverDoesntExists() throws Exception {
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