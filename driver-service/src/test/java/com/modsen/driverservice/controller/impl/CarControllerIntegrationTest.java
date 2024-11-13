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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.driverservice.IntegrationTestData.ID;
import static com.modsen.driverservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.driverservice.IntegrationTestData.SQL_INSERT_CAR_DRIVER;
import static com.modsen.driverservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static com.modsen.driverservice.IntegrationTestData.CAR_CREATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.CAR_CREATE_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.CAR_GET_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.CAR_UPDATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.CAR_UPDATE_RESPONSE_DTO;
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
class CarControllerIntegrationTest {

    private static final String POSTGRESQL_IMAGE_NAME = "postgres:latest";

    private static final String EUREKA_CLIENT_ENABLED_PROPERTY = "eureka.client.enabled";
    private static final String BOOLEAN_PROPERTY_VALUE = "false";

    @DynamicPropertySource
    private static void disableEureka(DynamicPropertyRegistry registry) {
        registry.add(EUREKA_CLIENT_ENABLED_PROPERTY, () -> BOOLEAN_PROPERTY_VALUE);
    }

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
    void updateCar_ReturnsUpdatedCarDto_UpdatedColorField() throws Exception {
        given()
                        .contentType(ContentType.JSON)
                        .body(CAR_UPDATE_REQUEST_DTO)

                .when()
                        .put(TestData.CAR_UPDATE_ENDPOINT, ID, ID)
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_UPDATE_RESPONSE_DTO)));
    }

    @Test
    void createCar_ReturnsCreatedCarDto_AllMandatoryFieldsInRequestBody() throws Exception {
        given()
                        .contentType(ContentType.JSON)
                        .body(CAR_CREATE_REQUEST_DTO)
                .when()
                        .post(TestData.CAR_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_CREATE_RESPONSE_DTO)));
    }

    @Test
    void safeDeleteCar_ReturnsNoContentStatusCode_DatabaseContainsSuchCarId() {
        given()
                .when()
                        .delete(TestData.CAR_DELETE_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getCarById_ReturnsCarDto_DatabaseContainsSuchCarId() throws Exception {
        given()
                .when()
                        .get(TestData.CAR_GET_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_GET_RESPONSE_DTO)));
    }

}
