package com.modsen.driverservice.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.TestData;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.service.DriverService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @Test
    void createDriver_ReturnsCreatedDriverDto_AllMandatoryFieldsInRequestBody() throws Exception {
        // Arrange
        when(driverService.createDriver(any()))
                .thenReturn(TestData.DRIVER_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TestData.DRIVER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.DRIVER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(TestData.DRIVER_RESPONSE_DTO)));
    }

    @Test
    void createDriver_ReturnsValidationError_BlankPhoneAndEmailFields() throws Exception {
        // Arrange
        when(driverService.createDriver(any()))
                .thenReturn(TestData.DRIVER_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TestData.DRIVER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_DRIVER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getPageDrivers_ReturnsPageDriverDto_ValidRequest() throws Exception {
        // Arrange
        when(driverService.getPageDrivers(anyInt(), anyInt()))
                .thenReturn(TestData.PAGE_DRIVER_RESPONSE_DTO);

        // Act and Assert
        mockMvc.perform(get(TestData.DRIVER_ENDPOINT)
                        .param("limit", String.valueOf(1))
                        .param("offset", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(TestData.PAGE_DRIVER_RESPONSE_DTO)));
    }

    @Test
    void updateDriverById_ReturnsUpdatedDriverDto_AllFieldsUpdated() throws Exception {
        // Arrange
        when(driverService.updateDriverById(anyLong(), any(DriverDto.class)))
                .thenReturn(TestData.DRIVER_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TestData.DRIVER_UPDATE_ENDPOINT, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.DRIVER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(TestData.DRIVER_RESPONSE_DTO)));
    }

    @Test
    void updateDriverById_ReturnValidationError_BlankEmailPhoneFields() throws Exception {
        // Arrange
        when(driverService.updateDriverById(anyLong(), any(DriverDto.class)))
                .thenReturn(TestData.DRIVER_RESPONSE_DTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TestData.DRIVER_UPDATE_ENDPOINT, TestData.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestData.INVALID_DRIVER_REQUEST_DTO));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void safeDeleteDriver_ReturnsNoContentStatusCode_ValidRequest() throws Exception {
        // Arrange
        doNothing().when(driverService).safeDeleteDriverByDriverId(anyLong());

        // Act and Assert
        mockMvc.perform(delete(TestData.DRIVER_DELETE_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void safeDeleteDriver_ReturnsNotFoundStatusCode_SuchDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(DriverNotFoundException.class)
                .when(driverService).safeDeleteDriverByDriverId(anyLong());

        //Act and Assert
        mockMvc.perform(delete(TestData.DRIVER_DELETE_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDriverById_ReturnsDriverDto_ValidRequest() throws Exception {
        // Arrange
        when(driverService.getDriverById(any()))
                .thenReturn(TestData.DRIVER_RESPONSE_DTO);

        // Act and Assert
        mockMvc.perform(get(TestData.DRIVER_GET_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(TestData.DRIVER_RESPONSE_DTO)));
    }

    @Test
    void getDriverById_ReturnsNotFoundStatusCode_SuchDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(new DriverNotFoundException("")).when(driverService).getDriverById(anyLong());

        //Act and Assert
        mockMvc.perform(get(TestData.DRIVER_GET_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getDriverWithCars_ReturnsDriverCarDto_ValidRequest() throws Exception {
        // Arrange
        when(driverService.getDriverWithCars(anyLong())).thenReturn(TestData.DRIVER_CAR_RESPONSE_DTO);

        // Act and Assert
        mockMvc.perform(get(TestData.DRIVER_CARS_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(TestData.DRIVER_CAR_RESPONSE_DTO)));
    }

    @Test
    void getDriverWithCars_ReturnsNotFoundStatusCode_SuchDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(DriverNotFoundException.class)
                .when(driverService).getDriverWithCars(anyLong());

        // Act and Assert
        mockMvc.perform(get(TestData.DRIVER_CARS_ENDPOINT, TestData.DRIVER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}

