package com.modsen.ridesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.http.ContentType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.modsen.ridesservice.IntegrationTestData.DRIVER_RESPONSE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.DRIVER_FROM_ANOTHER_SERVICE_ENDPOINT;
import static com.modsen.ridesservice.IntegrationTestData.PASSENGER_FROM_ANOTHER_SERVICE_ENDPOINT;
import static com.modsen.ridesservice.IntegrationTestData.PASSENGER_RESPONSE_DTO;

public class WireMockStubs {

    public static void stubForGettingDriverPassengerResponseDto(WireMockServer wireMockServer,
                                                                ObjectMapper objectMapper) throws Exception {
        wireMockServer.stubFor(get(urlEqualTo(DRIVER_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", ContentType.JSON.toString())
                        .withBody(objectMapper.writeValueAsString(PASSENGER_RESPONSE_DTO))));

        wireMockServer.stubFor(get(urlEqualTo(PASSENGER_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", ContentType.JSON.toString())
                        .withBody(objectMapper.writeValueAsString(DRIVER_RESPONSE_DTO))));
    }

}
