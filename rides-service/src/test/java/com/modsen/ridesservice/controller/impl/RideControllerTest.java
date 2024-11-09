package com.modsen.ridesservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ridesservice.AppTestUtil;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.service.RideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RideController.class)
class RideControllerTest {

    @MockBean
    private RideService rideService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPageRides_ReturnsPageRideDto_ValidRequest() throws Exception {
        //Arrange
        when(rideService.getPageRides(anyInt(), anyInt()))
                .thenReturn(AppTestUtil.RIDE_PAGE_RESPONSE_DTO);
        //Act and Assert
        mockMvc.perform(get(AppTestUtil.RIDE_PAGE_GET_POST_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_PAGE_RESPONSE_DTO)));
    }

    @Test
    void getRideById_ReturnsRideDto_ValidRequest() throws Exception {
        //Arrange
        when(rideService.getRideById(anyLong()))
                .thenReturn(AppTestUtil.RIDE_RESPONSE_DTO);

        //Act and Assert
        mockMvc.perform(get(AppTestUtil.RIDE_GET_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_RESPONSE_DTO)));
    }

    @Test
    void getPageRideByDriverId_ReturnsPageRideDto_ValidRequest() throws Exception {
        //Arrange
        when(rideService.getPageRidesByDriverId(anyLong(), anyInt(), anyInt()))
                .thenReturn(AppTestUtil.RIDE_PAGE_RESPONSE_DTO);

        //Act and Assert
        mockMvc.perform(get(AppTestUtil.RIDE_BY_DRIVER_ID_GET_ENDPOINT, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_PAGE_RESPONSE_DTO)));
    }

    @Test
    void getPageRideByPassengerId_ReturnsPageRideDto_ValidRequest() throws Exception {
        //Arrange
        when(rideService.getPageRidesByPassengerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(AppTestUtil.RIDE_PAGE_RESPONSE_DTO);

        //Act and Assert
        mockMvc.perform(get(AppTestUtil.RIDE_BY_PASSENGER_ID_GET_ENDPOINT, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_PAGE_RESPONSE_DTO)));
    }

    @Test
    void createRide_ReturnsCreatedRideDto_AllMandatoryFieldsInRequestBody() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.RIDE_PAGE_GET_POST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.RIDE_REQUEST_DTO));
        when(rideService.createRide(any(RideRequestDto.class)))
                .thenReturn(AppTestUtil.RIDE_RESPONSE_DTO);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_RESPONSE_DTO)));
    }

    @Test
    void changeRideStatus_ReturnsRideDtoWithUpdatedStatus_ValidRequest() throws Exception {
        //Arrange
        RideStatusRequestDto rideStatusRequestDto = new RideStatusRequestDto("ACCEPTED");
        RideResponseDto rideResponseDto = new RideResponseDto(
                1L, 1L, 1L,
                "Vilnius", "Riga", "ACCEPTED",
                LocalDateTime.parse("2023-10-31T14:30:00"), new BigDecimal(231));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(AppTestUtil.RIDE_CHANGE_RIDE_STATUS_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideStatusRequestDto));
        when(rideService.changeRideStatus(1L, rideStatusRequestDto))
                .thenReturn(rideResponseDto);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(rideResponseDto)));
    }

    @Test
    void updateRide_ReturnsUpdatedRideDto_ValidRequest() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.RIDE_UPDATE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.RIDE_REQUEST_DTO));
        when(rideService.updateRide(1L, AppTestUtil.RIDE_REQUEST_DTO))
                .thenReturn(AppTestUtil.RIDE_RESPONSE_DTO);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.RIDE_RESPONSE_DTO)));
    }

}