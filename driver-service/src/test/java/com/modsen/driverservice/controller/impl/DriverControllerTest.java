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
import com.modsen.driverservice.AppTestUtil;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.service.DriverService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DriverController.class)
@ExtendWith(SpringExtension.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @Test
    @DisplayName("Test createDriver(DriverDto); then success")
    void testCreateDriver_thenSuccess() throws Exception {
        // Arrange
        when(driverService.createDriver(any()))
                .thenReturn(AppTestUtil.driverResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.DRIVER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.driverRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.driverResponseDto)));
    }

    @Test
    @DisplayName("Test createDriver(DriverDto); with invalid parameters")
    void testCreateDriver_ShouldReturnValidationError_withInvalidParameters() throws Exception {
        // Arrange
        when(driverService.createDriver(any()))
                .thenReturn(AppTestUtil.driverResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AppTestUtil.DRIVER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidDriverRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test getPageDrivers(Integer, Integer); then success")
    void testGetPageDrivers_thenSuccess() throws Exception {
        // Arrange
        when(driverService.getPageDrivers(anyInt(), anyInt()))
                .thenReturn(AppTestUtil.pageDriverResponseDto);

        // Act and Assert
        mockMvc.perform(get(AppTestUtil.DRIVER_ENDPOINT)
                        .param("limit", String.valueOf(1))
                        .param("offset", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.pageDriverResponseDto)));
    }

    @Test
    @DisplayName("Test updateDriverById(Long, DriverDto); then success")
    void testUpdateDriverById_thenSuccess() throws Exception {
        // Arrange
        when(driverService.updateDriverById(anyLong(), any(DriverDto.class)))
                .thenReturn(AppTestUtil.driverResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.DRIVER_UPDATE_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.driverRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.driverResponseDto)));
    }

    @Test
    @DisplayName("Test updateDriverById((Long, DriverDto); with invalid parameters")
    void testUpdateDriverById_ShouldReturnValidationError_withInvalidParameters() throws Exception {
        // Arrange
        when(driverService.updateDriverById(anyLong(), any(DriverDto.class)))
                .thenReturn(AppTestUtil.driverResponseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(AppTestUtil.DRIVER_UPDATE_ENDPOINT, AppTestUtil.DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AppTestUtil.invalidDriverRequestDto));

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test safeDeleteDriver(Long); then success")
    void testSafeDeleteDriver_thenSuccess() throws Exception {
        // Arrange
        doNothing().when(driverService).safeDeleteDriverByDriverId(anyLong());

        // Act and Assert
        mockMvc.perform(delete(AppTestUtil.DRIVER_DELETE_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test safeDeleteDriver(Long); then returns NOT_FOUND status code")
    void testSafeDeleteDriver_thenReturnsNotFoundStatusCode_whenSuchDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(new DriverNotFoundException("")).when(driverService).safeDeleteDriverByDriverId(anyLong());

        //Act and Assert
        mockMvc.perform(delete(AppTestUtil.DRIVER_DELETE_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getDriverById(Long); then success")
    void testGetDriverById_thenSuccess() throws Exception {
        // Arrange
        when(driverService.getDriverById(any()))
                .thenReturn(AppTestUtil.driverResponseDto);

        // Act and Assert
        mockMvc.perform(get(AppTestUtil.DRIVER_GET_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.driverResponseDto)));
    }

    @Test
    @DisplayName("Test getDriverById(Long); then returns NOT_FOUND status code")
    void testGetDriverById_thenReturnsNotFoundStatusCode_whenDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(new DriverNotFoundException("")).when(driverService).getDriverById(anyLong());

        //Act and Assert
        mockMvc.perform(get(AppTestUtil.DRIVER_GET_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test getDriverWithCars(Long); then success")
    void testGetDriverWithCars_thenSuccess() throws Exception {
        // Arrange
        when(driverService.getDriverWithCars(anyLong())).thenReturn(AppTestUtil.driverCarResponseDto);

        // Act and Assert
        mockMvc.perform(get(AppTestUtil.DRIVER_CARS_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(AppTestUtil.driverCarResponseDto)));
    }

    @Test
    @DisplayName("Test getDriverWithCars(Long); then returns NOT_FOUND status code")
    void testGetDriverWithCars_thenReturnsNotFoundStatusCode_whenDriverDoesntExists() throws Exception {
        //Arrange
        doThrow(new DriverNotFoundException("")).when(driverService).getDriverWithCars(anyLong());

        // Act and Assert
        mockMvc.perform(get(AppTestUtil.DRIVER_CARS_ENDPOINT, AppTestUtil.DRIVER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}

