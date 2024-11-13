package com.modsen.ridesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ridesservice.client.driver.DriverResponseDto;
import com.modsen.ridesservice.client.passenger.PassengerResponseDto;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.modsen.ridesservice.IntegrationTestData.DRIVER_FROM_ANOTHER_SERVICE_ENDPOINT;
import static com.modsen.ridesservice.IntegrationTestData.PASSENGER_FROM_ANOTHER_SERVICE_ENDPOINT;

public class WireMockStubs {

    public static void stubForGettingDriverResponseDto(WireMockServer wireMockServer,
                                                       ObjectMapper objectMapper,
                                                       DriverResponseDto response) throws Exception {
        wireMockServer.stubFor(get(urlEqualTo(DRIVER_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))));
    }

    public static void stubForGettingPassengerResponseDto(WireMockServer wireMockServer,
                                                          ObjectMapper objectMapper,
                                                          PassengerResponseDto response) throws Exception {
        wireMockServer.stubFor(get(urlEqualTo(PASSENGER_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))));
    }

}
