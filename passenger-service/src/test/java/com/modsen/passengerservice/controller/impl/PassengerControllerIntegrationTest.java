package com.modsen.passengerservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.TestData;
import com.modsen.passengerservice.kafka.KafkaConsumerListener;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_ID;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_PAGE_GET_DTO;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_REQUEST_CREATE_DTO;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_REQUEST_UPDATE_DTO;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_RESPONSE_CREATE_DTO;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_RESPONSE_GET_DTO;
import static com.modsen.passengerservice.IntegrationTestData.PASSENGER_RESPONSE_UPDATE_DTO;
import static com.modsen.passengerservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.passengerservice.IntegrationTestData.SQL_INSERT_DATA;
import static com.modsen.passengerservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerListener.class})
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_DATA
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PassengerControllerIntegrationTest {

    private static final String POSTGRESQL_IMAGE_NAME = "postgres:latest";

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse(POSTGRESQL_IMAGE_NAME));

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void createPassenger_ReturnsNewPassengerDto_ContainsAllMandatoryFields() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(PASSENGER_REQUEST_CREATE_DTO))
                .when()
                    .post(TestData.PASSENGER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE_CREATE_DTO)));
    }

    @Test
    void getPagePassengers_ReturnsPageWithPassengerDto_DefaultOffsetAndLimit() throws Exception {
        given()
                .when()
                    .get(TestData.PASSENGER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(PASSENGER_PAGE_GET_DTO)));
    }

    @Test
    void updatePassengerById_ReturnsUpdatedPassengerDto_UpdatedPhoneField() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(PASSENGER_REQUEST_UPDATE_DTO))
                .when()
                    .put(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, PASSENGER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE_UPDATE_DTO)));
    }

    @Test
    void safeDeletePassenger_ReturnsNoContentStatusCode_DatabaseContainsSuchPassengerId() {
        given()
                .when()
                    .delete(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, PASSENGER_ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getPassengerById_ReturnsPassengerDto_DatabaseContainsSuchPassengerId() throws Exception {
        given()
                .when()
                    .get(TestData.PASSENGER_UPDATE_DELETE_ENDPOINT, PASSENGER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(PASSENGER_RESPONSE_GET_DTO)));
    }

}
