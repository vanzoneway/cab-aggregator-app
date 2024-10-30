package com.modsen.driverservice.controller.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.service.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CarController.class)
@ExtendWith(SpringExtension.class)
class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private static final Long DRIVER_ID = 1L;
    private static final Long CAR_ID = 1L;

    private static final String CAR_JSON =
            "{\"id\":1," +
                    "\"brand\":\"Brand\"," +
                    "\"color\":\"Color\"," +
                    "\"number\":\"ABC123\"," +
                    "\"model\":\"Model\"," +
                    "\"year\":1," +
                    "\"deleted\":true}";
    private static final String CAR_ENDPOINT = "/api/v1/cars/drivers/{driverId}";
    private static final String CAR_UPDATE_ENDPOINT = "/api/v1/cars/{carId}/drivers/{driverId}";
    private static final String CAR_DELETE_ENDPOINT = "/api/v1/cars/{carId}";
    private static final String CAR_GET_ENDPOINT = "/api/v1/cars/{carId}";

    @Test
    @DisplayName("Test createCar(Long, CarDto)")
    void testCreateCar() throws Exception {
        // Arrange
        when(carService.createCar(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(createCarDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CAR_ENDPOINT, DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CAR_JSON);

        // Act
        ResultActions actualPerformResult = mockMvc.perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(CAR_JSON));
    }

    @Test
    @DisplayName("Test updateCarByCarIdAndDriverId(Long, Long, CarDto)")
    void testUpdateCarByCarIdAndDriverId() throws Exception {
        // Arrange
        when(carService.updateCarByCarIdAndDriverId(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<CarDto>any()))
                .thenReturn(createCarDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CAR_UPDATE_ENDPOINT, CAR_ID, DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CAR_JSON);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(CAR_JSON));
    }

    @Test
    @DisplayName("Test safeDeleteCarById(Long)")
    void testSafeDeleteCarById() throws Exception {
        // Arrange
        doNothing().when(carService).safeDeleteCarByCarId(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CAR_DELETE_ENDPOINT, CAR_ID);

        // Act
        ResultActions actualPerformResult = mockMvc.perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test getCarById(Long)")
    void testGetCarById() throws Exception {
        // Arrange
        when(carService.getCarById(Mockito.<Long>any()))
                .thenReturn(createCarDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CAR_GET_ENDPOINT, CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(CAR_JSON));
    }

    private CarDto createCarDto() {
        return new CarDto(1L, "Brand", "Color", "ABC123", "Model", 1, true);
    }
}
