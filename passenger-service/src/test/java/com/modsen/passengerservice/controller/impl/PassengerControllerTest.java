package com.modsen.passengerservice.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.AppTestUtil;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.service.PassengerService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;

    @Test
    @DisplayName("Test createPassenger(PassengerDto); then success")
    void testCreatePassenger_thenSuccess() throws Exception {
        // Arrange
        when(passengerService.createPassenger(any(PassengerDto.class)))
                .thenReturn(AppTestUtil.passengerResponseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(AppTestUtil.PASSENGER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.passengerRequestDto));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.passengerResponseDto)));
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto); then returns BAD_REQUEST status code")
    void testCreatePassenger_thenReturnsBadRequestStatusCode_withInvalidParameters() throws Exception {
        // Arrange
        when(passengerService.createPassenger(any(PassengerDto.class)))
                .thenReturn(AppTestUtil.passengerResponseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(AppTestUtil.PASSENGER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidPassengerRequestDto));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test getPagePassengers(Integer, Integer); then success")
    void testGetPagePassengers_thenSuccess() throws Exception {
        // Arrange
        when(passengerService.getPagePassengers(anyInt(), anyInt()))
                .thenReturn(AppTestUtil.passengerPageResponseDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.PASSENGER_ENDPOINT)
                .param("limit", String.valueOf(1))
                .param("offset", String.valueOf(1));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.passengerPageResponseDto)));
    }

    @Test
    @DisplayName("Test updatePassengerById(Long, PassengerDto); then success")
    void testUpdatePassengerById_thenSuccess() throws Exception {
        // Arrange
        when(passengerService.updatePassengerById(any(Long.class), Mockito.any(PassengerDto.class)))
                .thenReturn(AppTestUtil.passengerResponseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.passengerRequestDto));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        AppTestUtil.passengerResponseDto)));
    }

    @Test
    @DisplayName("Test updatePassengerById(Long, PassengerDto); then returns BAD_REQUEST status code")
    void testUpdatePassengerById_thenReturnsBadRequestStatusCode_withInvalidParameters() throws Exception {
        // Arrange
        when(passengerService.updatePassengerById(any(Long.class), Mockito.any(PassengerDto.class)))
                .thenReturn(AppTestUtil.passengerResponseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidPassengerRequestDto));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test safeDeletePassenger(Long); then success")
    void testSafeDeletePassenger_thenSuccess() throws Exception {
        // Arrange
        doNothing().when(passengerService).safeDeletePassengerById(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test safeDeletePassenger(Long); then returns NOT_FOUND status code")
    void testSafeDeletePassenger_thenReturnsNotFoundStatusCode_withValidParameters() throws Exception {
        // Arrange
        doThrow(new PassengerNotFoundException("")).when(passengerService).safeDeletePassengerById(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test getPassengerById(Long); then success")
    void testGetPassengerById_thenSuccess() throws Exception {
        // Arrange
        when(passengerService.getPassengerById(anyLong()))
                .thenReturn(AppTestUtil.passengerResponseDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .string(objectMapper.writeValueAsString(
                                AppTestUtil.passengerResponseDto)));
    }

    @Test
    @DisplayName("Test getPassengerById(Long); then returns NOT_FOUND status code")
    void testGetPassengerById_thenReturnsNotFoundStatusCode_withValidParameters() throws Exception {
        // Arrange
        when(passengerService.getPassengerById(anyLong()))
                .thenThrow(new PassengerNotFoundException(""));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
