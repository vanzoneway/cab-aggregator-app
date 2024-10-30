package com.modsen.driverservice.controller.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.service.DriverService;

import java.util.ArrayList;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DriverController.class)
@ExtendWith(SpringExtension.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    private static final Long DRIVER_ID = 1L;

    private static final String DRIVER_JSON =
            "{" +
                    "\"id\":1," +
                    "\"name\":\"Name\"," +
                    "\"email\":\"jane.doe@example.org\"," +
                    "\"phone\":\"6625550144\"," +
                    "\"gender\":\"Gender\"," +
                    "\"averageRating\":null," +
                    "\"deleted\":false" +
                    "}";

    private static final String DRIVER_PAGE_RESPONSE_JSON =
            "{" +
                    "\"currentOffset\":1," +
                    "\"currentLimit\":1," +
                    "\"totalPages\":1," +
                    "\"totalElements\":1," +
                    "\"sort\":\"Sort\"," +
                    "\"values\":[]" +
                    "}";

    private static final String DRIVER_ENDPOINT = "/api/v1/drivers";
    private static final String DRIVER_UPDATE_ENDPOINT = "/api/v1/drivers/{driverId}";
    private static final String DRIVER_DELETE_ENDPOINT = "/api/v1/drivers/{driverId}";
    private static final String DRIVER_GET_ENDPOINT = "/api/v1/drivers/{driverId}";
    private static final String DRIVER_CARS_ENDPOINT = "/api/v1/drivers/{driverId}/cars";

    @Test
    @DisplayName("Test createDriver(DriverDto)")
    void testCreateDriver() throws Exception {
        // Arrange
        when(driverService.createDriver(Mockito.any()))
                .thenReturn(new DriverDto(
                        DRIVER_ID,
                        "Name",
                        "jane.doe@example.org",
                        "6625550144",
                        "Gender",
                        null,
                        false));


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(DRIVER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DRIVER_JSON);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(DRIVER_JSON));
    }

    @Test
    @DisplayName("Test getPageDrivers(Integer, Integer)")
    void testGetPageDrivers() throws Exception {
        // Arrange
        when(driverService.getPageDrivers(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenReturn(new ListContainerResponseDto<>(1, 1, 1, 1L,
                        "Sort", new ArrayList<>()));

        // Act and Assert
        mockMvc.perform(get(DRIVER_ENDPOINT)
                        .param("limit", String.valueOf(1))
                        .param("offset", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(DRIVER_PAGE_RESPONSE_JSON));
    }

    @Test
    @DisplayName("Test updateDriverById(Long, DriverDto)")
    void testUpdateDriverById() throws Exception {
        // Arrange
        when(driverService.updateDriverById(Mockito.<Long>any(), Mockito.<DriverDto>any()))
                .thenReturn(new DriverDto(
                        DRIVER_ID,
                        "Name",
                        "jane.doe@example.org",
                        "6625550144",
                        "Gender",
                        null,
                        false));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(DRIVER_UPDATE_ENDPOINT, DRIVER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DRIVER_JSON);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(DRIVER_JSON));
    }

    @Test
    @DisplayName("Test safeDeleteDriver(Long)")
    void testSafeDeleteDriver() throws Exception {
        // Arrange
        doNothing().when(driverService).safeDeleteDriverByDriverId(Mockito.<Long>any());

        // Act and Assert
        mockMvc.perform(delete(DRIVER_DELETE_ENDPOINT, DRIVER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test getDriverById(Long)")
    void testGetDriverById() throws Exception {
        // Arrange
        DriverDto driverDto = new DriverDto(
                DRIVER_ID,
                "Name",
                "jane.doe@example.org",
                "6625550144",
                "Gender",
                10.0d,
                false);
        when(driverService.getDriverById(Mockito.<Long>any()))
                .thenReturn(driverDto);

        // Act and Assert
        mockMvc.perform(get(DRIVER_GET_ENDPOINT, DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(driverDto)));
    }

    @Test
    @DisplayName("Test getDriverWithCars(Long)")
    void testGetDriverWithCars() throws Exception {
        // Arrange
        DriverCarDto driverCarDto = new DriverCarDto();
        driverCarDto.setAverageRating(10.0d);
        driverCarDto.setCars(new ArrayList<>());
        driverCarDto.setEmail("jane.doe@example.org");
        driverCarDto.setGender("Gender");
        driverCarDto.setId(DRIVER_ID);
        driverCarDto.setName("Name");
        driverCarDto.setPhone("6625550144");

        when(driverService.getDriverWithCars(Mockito.<Long>any())).thenReturn(driverCarDto);

        // Act and Assert
        mockMvc.perform(get(DRIVER_CARS_ENDPOINT, DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(driverCarDto)));
    }

}

