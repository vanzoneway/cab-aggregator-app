package com.modsen.ratingservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ratingservice.AppTestUtil;
import com.modsen.ratingservice.kafka.KafkaProducerSender;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.ratingservice.AppIntegrationTestUtil.AVERAGE_RATING_RESPONSE_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_ID;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_REQUEST_CREATE_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_REQUEST_UPDATE_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_RESPONSE_CREATE_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_RESPONSE_GET_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.RATING_RESPONSE_UPDATE_DTO;
import static com.modsen.ratingservice.AppIntegrationTestUtil.REF_USER_ID;
import static com.modsen.ratingservice.AppIntegrationTestUtil.SQL_DELETE_ALL_DATA;
import static com.modsen.ratingservice.AppIntegrationTestUtil.SQL_INSERT_DATA;
import static com.modsen.ratingservice.AppIntegrationTestUtil.SQL_RESTART_SEQUENCES;
import static com.modsen.ratingservice.WireMockStubs.stubForGettingRideResponseDto;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaProducerSender.class})
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 9090)
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_DATA
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DriverRatingControllerIntegrationTest {

    private static final String POSTGRESQL_IMAGE_NAME = "postgres:latest";

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse(POSTGRESQL_IMAGE_NAME));

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void createDriverRating_withValidParams_thenSuccess() throws Exception {
        stubForGettingRideResponseDto(wireMockServer, objectMapper);
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(RATING_REQUEST_CREATE_DTO))
                .when()
                        .post(AppTestUtil.DRIVER_RATING_ENDPOINT)
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                            .contentType(ContentType.JSON)
                            .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_CREATE_DTO)));
    }

    @Test
    void getAverageRating_withValidParams_thenSuccess() throws Exception {

        given()
                .when()
                    .get(AppTestUtil.DRIVER_RATING_AVERAGE_ENDPOINT, REF_USER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(AVERAGE_RATING_RESPONSE_DTO)));
    }

    @Test
    void updateDriverRating_withValidParams_thenSuccess() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(RATING_REQUEST_UPDATE_DTO))
                .when()
                    .put(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_UPDATE_DTO)));
    }

    @Test
    void safeDeleteDriverRating_withValidParams_thenSuccess() {
        given()
                .when()
                    .delete(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getDriverRating_withValidParams_thenSuccess() throws Exception {
        given()
                .when()
                    .get(AppTestUtil.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_GET_DTO)));
    }

}
