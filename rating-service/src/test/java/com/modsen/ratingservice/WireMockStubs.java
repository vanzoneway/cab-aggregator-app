package com.modsen.ratingservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.client.RideResponseDto;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.modsen.ratingservice.IntegrationTestData.GET_RIDE_FROM_ANOTHER_SERVICE_ENDPOINT;

public class WireMockStubs {

    public static void stubForGettingRideResponseDto(WireMockServer wireMockServer,
                                                     ObjectMapper objectMapper,
                                                     RideResponseDto response)
            throws Exception {
        wireMockServer.stubFor(get(urlEqualTo(GET_RIDE_FROM_ANOTHER_SERVICE_ENDPOINT))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(response))));
    }

}
