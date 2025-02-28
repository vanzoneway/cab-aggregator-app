package com.modsen.driverservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregatorexceptionspringbootstarter.exception.BasicGlobalExceptionHandler;
import com.modsen.driverservice.TestData;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(CarController.class)
@Import({BasicGlobalExceptionHandler.class})
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
                .thenReturn(TestData.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TestData.CAR_ENDPOINT, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.CAR_REQUEST_DTO))
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(TestData.CAR_RESPONSE_DTO)));
    }

    @Test
    void createCar_ReturnsValidationError_InvalidCarNumberFormatAndNotNullDeletedField() throws Exception {
        //Arrange
        when(carService.createCar(anyLong(), any(CarDto.class)))
                .thenReturn(TestData.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TestData.CAR_ENDPOINT, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_CAR_REQUEST_DTO))
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCarByCarIdAndDriverId_ReturnsUpdatedCarDto_AllFieldsUpdated() throws Exception {
        // Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(TestData.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TestData.CAR_UPDATE_ENDPOINT, TestData.CAR_ID, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.CAR_REQUEST_DTO))
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(TestData.CAR_RESPONSE_DTO)));
    }

    @Test
    void updateCarByCarIdAndDriverId_ReturnsValidationError_InvalidCarNumberFormat()
            throws Exception {
        //Arrange
        when(carService.updateCarByCarIdAndDriverId(anyLong(), anyLong(), any(CarDto.class)))
                .thenReturn(TestData.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TestData.CAR_UPDATE_ENDPOINT, TestData.CAR_ID, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_CAR_REQUEST_DTO))
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

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
                .delete(TestData.CAR_DELETE_ENDPOINT, TestData.CAR_ID)
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

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
                .delete(TestData.CAR_DELETE_ENDPOINT, TestData.CAR_ID)
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getCarById_ReturnsCarDto_ValidRequest() throws Exception {
        // Arrange
        when(carService.getCarById(anyLong()))
                .thenReturn(TestData.CAR_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TestData.CAR_GET_ENDPOINT, TestData.CAR_ID)
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(TestData.CAR_RESPONSE_DTO)
                ));
    }

    @Test
    void getCarById_ReturnsNotFoundStatusCode_SuchCarDoesntExists() throws Exception {
        //Arrange
        doThrow(CarNotFoundException.class)
                .when(carService).getCarById(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TestData.CAR_GET_ENDPOINT, TestData.CAR_ID)
                .with(jwt().authorities(new SimpleGrantedAuthority(TestData.SECURITY_ADMIN_ROLE)));

        //Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
