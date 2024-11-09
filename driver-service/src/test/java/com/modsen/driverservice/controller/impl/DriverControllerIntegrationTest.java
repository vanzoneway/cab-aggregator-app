package com.modsen.driverservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.TestData;
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

import static com.modsen.driverservice.IntegrationTestData.ID;
import static com.modsen.driverservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.driverservice.IntegrationTestData.SQL_INSERT_CAR_DRIVER;
import static com.modsen.driverservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_CAR_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_CREATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_CREATE_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_PAGE_GET_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_UPDATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_UPDATE_RESPONSE_DTO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({KafkaConsumerListener.class})
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_CAR_DRIVER
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
    void createDriver_ReturnsCreatedDriverDto_AllMandatoryParamsInRequestBody() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(DRIVER_CREATE_REQUEST_DTO))
                .when()
                    .post(TestData.DRIVER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_CREATE_RESPONSE_DTO)));
    }

    @Test
    void getPageDrivers_ReturnsPageWithDriverDto_DefaultOffsetAndLimit() throws Exception {
        given()
                .when()
                    .get(TestData.DRIVER_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_PAGE_GET_RESPONSE_DTO)));
    }

    @Test
    void updateDriver_ReturnsUpdatedDriverDto_UpdatedNameField() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(DRIVER_UPDATE_REQUEST_DTO))
                .when()
                    .put(TestData.DRIVER_UPDATE_ENDPOINT, TestData.DRIVER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_UPDATE_RESPONSE_DTO)));
    }

    @Test
    void safeDeleteDriver_ReturnsNoContentStatusCode_DatabaseContainsSuchDriverId() {
        given()
                .when()
                    .delete(TestData.DRIVER_DELETE_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getDriverWithCars_ReturnsDriverCarDto_DatabaseContainsSuchDriverId() throws Exception {
        given()
                .when()
                    .get(TestData.DRIVER_CARS_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(equalTo(objectMapper.writeValueAsString(DRIVER_CAR_RESPONSE_DTO)));
    }

}
