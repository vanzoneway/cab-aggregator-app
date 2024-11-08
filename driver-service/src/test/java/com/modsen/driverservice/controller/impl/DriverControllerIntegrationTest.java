package com.modsen.driverservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.AppTestUtil;
import com.modsen.driverservice.kafka.KafkaConsumerListener;
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

import static com.modsen.driverservice.AppIntegrationTestUtil.ID;
import static com.modsen.driverservice.AppIntegrationTestUtil.SQL_DELETE_ALL_DATA;
import static com.modsen.driverservice.AppIntegrationTestUtil.SQL_INSERT_CAR;
import static com.modsen.driverservice.AppIntegrationTestUtil.SQL_RESTART_SEQUENCES;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_CAR_RESPONSE_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_CREATE_REQUEST_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_CREATE_RESPONSE_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_PAGE_GET_RESPONSE_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_UPDATE_REQUEST_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.DRIVER_UPDATE_RESPONSE_DTO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerListener.class})
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_CAR
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DriverControllerIntegrationTest {

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
    void createDriver_withValidParams_thenSuccess() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(DRIVER_CREATE_REQUEST_DTO))
                .when()
                    .post(AppTestUtil.DRIVER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_CREATE_RESPONSE_DTO)));
    }

    @Test
    void getPageDrivers_withValidParams_thenSuccess() throws Exception {
        given()
                .when()
                    .get(AppTestUtil.DRIVER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_PAGE_GET_RESPONSE_DTO)));
    }

    @Test
    void updateDriver_withValidParams_thenSuccess() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(DRIVER_UPDATE_REQUEST_DTO))
                .when()
                    .put(AppTestUtil.DRIVER_UPDATE_ENDPOINT, AppTestUtil.DRIVER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_UPDATE_RESPONSE_DTO)));
    }

    @Test
    void safeDeleteDriver_withValidParams_thenSuccess() {
        given()
                .when()
                    .delete(AppTestUtil.DRIVER_DELETE_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getDriverWithCars_withValidParams_thenSuccess() throws Exception {
        given()
                .when()
                    .get(AppTestUtil.DRIVER_CARS_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_CAR_RESPONSE_DTO)));
    }

}
