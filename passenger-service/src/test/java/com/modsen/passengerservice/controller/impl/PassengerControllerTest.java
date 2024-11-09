package com.modsen.passengerservice.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.TestData;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.service.PassengerService;

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
    void createPassenger_ReturnsCreatedPassengerDto_AllMandatoryFieldsInRequestBody() throws Exception {
        // Arrange
        when(passengerService.createPassenger(any(PassengerDto.class)))
                .thenReturn(TestData.PASSENGER_RESPONSE_DTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(TestData.PASSENGER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.PASSENGER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        TestData.PASSENGER_RESPONSE_DTO)));
    }

    @Test
    void createPassenger_ReturnsValidationError_AllMandatoryFieldsAreBlank() throws Exception {
        // Arrange
        when(passengerService.createPassenger(any(PassengerDto.class)))
                .thenReturn(TestData.PASSENGER_RESPONSE_DTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(TestData.PASSENGER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_PASSENGER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getPagePassengers_ReturnsPagePassengersDto_ValidRequest() throws Exception {
        // Arrange
        when(passengerService.getPagePassengers(anyInt(), anyInt()))
                .thenReturn(TestData.PASSENGER_PAGE_RESPONSE_DTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TestData.PASSENGER_ENDPOINT)
                .param("limit", String.valueOf(1))
                .param("offset", String.valueOf(1));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        TestData.PASSENGER_PAGE_RESPONSE_DTO)));
    }

    @Test
    void updatePassengerById_ReturnsUpdatedPassengerDto_ValidRequest() throws Exception {
        // Arrange
        when(passengerService.updatePassengerById(any(Long.class), Mockito.any(PassengerDto.class)))
                .thenReturn(TestData.PASSENGER_RESPONSE_DTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.PASSENGER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        TestData.PASSENGER_RESPONSE_DTO)));
    }

    @Test
    void updatePassengerById_ReturnsValidationError_AllMandatoryFieldsAreBlank() throws Exception {
        // Arrange
        when(passengerService.updatePassengerById(any(Long.class), Mockito.any(PassengerDto.class)))
                .thenReturn(TestData.PASSENGER_RESPONSE_DTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_PASSENGER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void safeDeletePassenger_ReturnsNoContentStatusCode_ValidRequest() throws Exception {
        // Arrange
        doNothing().when(passengerService).safeDeletePassengerById(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void safeDeletePassenger_ReturnsNotFoundStatusCode_SuchPassengerDoesntExists() throws Exception {
        // Arrange
        doThrow(PassengerNotFoundException.class)
                .when(passengerService).safeDeletePassengerById(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getPassengerById_ReturnsPassengerDto_ValidRequest() throws Exception {
        // Arrange
        when(passengerService.getPassengerById(anyLong()))
                .thenReturn(TestData.PASSENGER_RESPONSE_DTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .string(objectMapper.writeValueAsString(
                                TestData.PASSENGER_RESPONSE_DTO)));
    }

    @Test
    void getPassengerById_ReturnsNotFoundStatusCode_SuchPassengerDoesntExists() throws Exception {
        // Arrange
        when(passengerService.getPassengerById(anyLong()))
                .thenThrow(new PassengerNotFoundException(""));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
