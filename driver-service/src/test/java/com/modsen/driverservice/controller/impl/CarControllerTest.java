package com.modsen.driverservice.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.AppTestUtil;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.service.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    @DisplayName("Test createCar(Long, CarDto); then success")
    void testCreateCar_thenSuccess() throws Exception {
        // Arrange
        when(carService.createCar(anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.carResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.CAR_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.carRequestDto));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.carResponseDto)));
    }

    @Test
    @DisplayName("Test createCar(Long, CarDto); with invalid parameters")
    void testCreateCar_ShouldReturnValidationError_withInvalidParameters() throws Exception {
        //Arrange
        when(carService.createCar(anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.carResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.CAR_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidCarRequestDto));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @DisplayName("Test updateCarByCarIdAndDriverId(Long, Long, CarDto); then success")
    void testUpdateCarByCarIdAndDriverId_thenSuccess() throws Exception {
        // Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.carResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.CAR_UPDATE_ENDPOINT, AppTestUtil.CAR_ID, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.carRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.carResponseDto)));
    }

    @Test
    @DisplayName("Test updateCarByCarIdAndDriverId(Long, Long, CarDto); with invalid parameters")
    void testUpdateCarByCarIdAndDriverId_ShouldReturnValidationError_withInvalidParameters() throws Exception {
        //Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.carResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.CAR_UPDATE_ENDPOINT, AppTestUtil.CAR_ID, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidCarRequestDto));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Test safeDeleteCarById(Long); then success")
    void testSafeDeleteCarById_thenSuccess() throws Exception {
        // Arrange
        doNothing().when(carService).safeDeleteCarByCarId(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.CAR_DELETE_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test safeDeleteCarById(Long); then returns NOT_FOUND status code")
    void testSafeDeleteCarById_thenReturnsNotFoundStatusCode_whenSuchCarDoesntExists() throws Exception {
        //Arrange
        doThrow(new CarNotFoundException("")).when(carService).safeDeleteCarByCarId(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.CAR_DELETE_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test getCarById(Long); then success")
    void testGetCarById_thenSuccess() throws Exception {
        // Arrange
        when(carService.getCarById(anyLong()))
                .thenReturn(AppTestUtil.carResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.CAR_GET_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.carResponseDto)
                ));
    }

    @Test
    @DisplayName("Test getCarById(Long); then returns NOT_FOUND status code")
    void testGetCarById_thenReturnsNotFoundStatusCode_whenSuchCarDoesntExists() throws Exception {
        //Arrange
        doThrow(new CarNotFoundException("")).when(carService).getCarById(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.CAR_GET_ENDPOINT, AppTestUtil.CAR_ID);
        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
