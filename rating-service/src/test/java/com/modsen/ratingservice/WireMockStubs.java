package com.modsen.ratingservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.modsen.ratingservice.AppIntegrationTestUtil.GET_RIDE_FROM_ANOTHER_SERVICE_ENDPOINT;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RIDE_RESPONSE_DTO;

public class WireMockStubs {

    public static void stubForGettingRideResponseDto(WireMockServer wireMockServer, ObjectMapper objectMapper)
            throws Exception {
        wireMockServer.stubFor(get(urlEqualTo(GET_RIDE_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader("Content-Type", ContentType.JSON.toString())
                        .withBody(objectMapper.writeValueAsString(RIDE_RESPONSE_DTO))));
    }
}
