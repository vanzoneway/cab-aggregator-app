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
    void createCar_ReturnsCreatedCarDto_AllMandatoryFieldsInRequestBody() throws Exception {
        // Arrange
        when(carService.createCar(anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.CAR_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.CAR_REQUEST_DTO));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.CAR_RESPONSE_DTO)));
    }

    @Test
    void createCar_ReturnsValidationError_InvalidCarNumberFormatAndNotNullDeletedField() throws Exception {
        //Arrange
        when(carService.createCar(anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.CAR_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.INVALID_CAR_REQUEST_DTO));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCarByCarIdAndDriverId_ReturnsUpdatedCarDto_AllFieldsUpdated() throws Exception {
        // Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.CAR_UPDATE_ENDPOINT, AppTestUtil.CAR_ID, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.CAR_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.CAR_RESPONSE_DTO)));
    }

    @Test
    void updateCarByCarIdAndDriverId_ReturnsValidationError_InvalidCarNumberFormat()
            throws Exception {
        //Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(AppTestUtil.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.CAR_UPDATE_ENDPOINT, AppTestUtil.CAR_ID, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.INVALID_CAR_REQUEST_DTO));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void safeDeleteCarById_ReturnsNoContentStatusCode_ValidRequest() throws Exception {
        // Arrange
        doNothing().when(carService).safeDeleteCarByCarId(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.CAR_DELETE_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void safeDeleteCarById_ReturnsNotFoundStatusCode_SuchCarDoesntExists() throws Exception {
        //Arrange
        doThrow(CarNotFoundException.class)
                .when(carService).safeDeleteCarByCarId(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(AppTestUtil.CAR_DELETE_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getCarById_ReturnsCarDto_ValidRequest() throws Exception {
        // Arrange
        when(carService.getCarById(anyLong()))
                .thenReturn(AppTestUtil.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.CAR_GET_ENDPOINT, AppTestUtil.CAR_ID);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(AppTestUtil.CAR_RESPONSE_DTO)
                ));
    }

    @Test
    void getCarById_ReturnsNotFoundStatusCode_SuchCarDoesntExists() throws Exception {
        //Arrange
        doThrow(CarNotFoundException.class)
                .when(carService).getCarById(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(AppTestUtil.CAR_GET_ENDPOINT, AppTestUtil.CAR_ID);
        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
