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
import static com.modsen.driverservice.AppIntegrationTestUtil.SQL_INSERT_CAR_DRIVER;
import static com.modsen.driverservice.AppIntegrationTestUtil.SQL_RESTART_SEQUENCES;
import static com.modsen.driverservice.AppIntegrationTestUtil.CAR_CREATE_REQUEST_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.CAR_CREATE_RESPONSE_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.CAR_GET_RESPONSE_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.CAR_UPDATE_REQUEST_DTO;
import static com.modsen.driverservice.AppIntegrationTestUtil.CAR_UPDATE_RESPONSE_DTO;
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
    void updateCar_withValidParams_thenSuccess() throws Exception {
        given()
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(CAR_UPDATE_REQUEST_DTO))

                .when()
                        .put(AppTestUtil.CAR_UPDATE_ENDPOINT, ID, ID)
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_UPDATE_RESPONSE_DTO)));
    }

    @Test
    void createCar_withValidParams_thenSuccess() throws Exception {
        given()
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(CAR_CREATE_REQUEST_DTO))
                .when()
                        .post(AppTestUtil.CAR_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_CREATE_RESPONSE_DTO)));
    }

    @Test
    void safeDeleteCar_withValidParams_thenSuccess() {
        given()
                .when()
                        .delete(AppTestUtil.CAR_DELETE_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getCarById_withValidParams_thenSuccess() throws Exception {
        given()
                .when()
                        .get(AppTestUtil.CAR_GET_ENDPOINT, ID)
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .contentType(ContentType.JSON)
                        .body(equalTo(objectMapper.writeValueAsString(CAR_GET_RESPONSE_DTO)));
    }

}
