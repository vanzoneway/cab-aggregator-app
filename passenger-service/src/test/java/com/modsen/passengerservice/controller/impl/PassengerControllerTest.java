package com.modsen.passengerservice.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.service.PassengerService;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(PassengerController.class)
@ExtendWith(SpringExtension.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    private static final String PASSENGER_RESPONSE_JSON =
            "{\"id\":1," +
                    "\"firstName\":\"Jane\"," +
                    "\"lastName\":\"Doe\"," +
                    "\"email\":\"jane.doe@example.org\"," +
                    "\"phone\":\"6625550144\"," +
                    "\"averageRating\":10.0," +
                    "\"deleted\":false}";
    private static final String PASSENGER_PAGE_RESPONSE_JSON =
            "{\"currentOffset\":1," +
                    "\"currentLimit\":1," +
                    "\"totalPages\":1," +
                    "\"totalElements\":1," +
                    "\"sort\":\"Sort\"," +
                    "\"values\":[]}";
    private static final String PASSENGER_ENDPOINT = "/api/v1/passengers";
    private static final String PASSENGER_UPDATE_DELETE_ENDPOINT = "/api/v1/passengers/{passengerId}";

    @Test
    @DisplayName("Test createPassenger(PassengerDto)")
    void testCreatePassenger() throws Exception {
        // Arrange
        PassengerDto passengerRequestDto = new PassengerDto(
                null,
                "Jane",
                "Doe",
                "jane.doe@example.org",
                "6625550144",
                null,
                null);
        PassengerDto passengerResponseDto = new PassengerDto(
                1L,
                "Jane",
                "Doe",
                "jane.doe@example.org",
                "6625550144",
                10d,
                false);
        when(passengerService.createPassenger(any(PassengerDto.class)))
                .thenReturn(passengerResponseDto);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post(PASSENGER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(passengerRequestDto));

        // Act
        ResultActions actualPerformResult = mockMvc.perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(PASSENGER_RESPONSE_JSON));
    }

    @Test
    @DisplayName("Test getPagePassengers(Integer, Integer)")
    void testGetPagePassengers() throws Exception {
        // Arrange
        when(passengerService.getPagePassengers(any(Integer.class), any(Integer.class)))
                .thenReturn(new ListContainerResponseDto<>(
                        1, 1, 1, 1L, "Sort", new ArrayList<>())
                );
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PASSENGER_ENDPOINT)
                .param("limit", String.valueOf(1))
                .param("offset", String.valueOf(1));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(PASSENGER_PAGE_RESPONSE_JSON));
    }

    @Test
    @DisplayName("Test updatePassengerById(Long, PassengerDto)")
    void testUpdatePassengerById() throws Exception {
        // Arrange
        PassengerDto passengerRequestDto = new PassengerDto(
                null,
                "Jane",
                "Doe",
                "jane.doe@example.org",
                "6625550144",
                null,
                null);
        PassengerDto passengerResponseDto = new PassengerDto(
                1L,
                "Jane",
                "Doe",
                "jane.doe@example.org",
                "6625550144",
                10d,
                false);
        when(passengerService.updatePassengerById(any(Long.class), Mockito.any(PassengerDto.class)))
                .thenReturn(passengerResponseDto);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put(PASSENGER_UPDATE_DELETE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(passengerRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(PASSENGER_RESPONSE_JSON));
    }

    @Test
    @DisplayName("Test safeDeletePassenger(Long)")
    void testSafeDeletePassenger() throws Exception {
        // Arrange
        doNothing().when(passengerService).safeDeletePassengerById(any(Long.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act
        ResultActions actualPerformResult = mockMvc.perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test getPassengerById(Long)")
    void testGetPassengerById() throws Exception {
        // Arrange
        when(passengerService.getPassengerById(Mockito.<Long>any()))
                .thenReturn(new PassengerDto(
                        1L,
                        "Jane",
                        "Doe",
                        "jane.doe@example.org",
                        "6625550144",
                        10.0d,
                        false));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PASSENGER_UPDATE_DELETE_ENDPOINT, 1L);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(PASSENGER_RESPONSE_JSON));
    }
}
