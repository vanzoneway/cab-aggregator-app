package com.modsen.ridesservice.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ridesservice.AppTestUtil;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.ridesservice.IntegrationTestData.IGNORING_FIELD_ONE;
import static com.modsen.ridesservice.IntegrationTestData.IGNORING_FIELD_TWO;
import static com.modsen.ridesservice.IntegrationTestData.INNER_IGNORING_FIELD_ONE;
import static com.modsen.ridesservice.IntegrationTestData.INNER_IGNORING_FIELD_TWO;
import static com.modsen.ridesservice.IntegrationTestData.PAGE_RIDE_RESPONSE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_ID;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_REQUEST_CREATE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_REQUEST_UPDATE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_RESPONSE_AFTER_CHANGE_STATUS_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_RESPONSE_CREATE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_RESPONSE_GET_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_RESPONSE_UPDATE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.RIDE_STATUS_CHANGE_REQUEST_DTO;
import static com.modsen.ridesservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.ridesservice.IntegrationTestData.SQL_INSERT_DATA;
import static com.modsen.ridesservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static com.modsen.ridesservice.WireMockStubs.stubForGettingDriverPassengerResponseDto;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 9091)
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_DATA
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RideControllerIntegrationTest {

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
    void getRideById_ReturnsRideDto_DatabaseContainsSuchRideId() throws Exception {
        Response response = given()
                .when()
                    .get(AppTestUtil.RIDE_GET_ENDPOINT)
                .then()
                    .contentType(ContentType.JSON)
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        RideResponseDto actual = objectMapper.readValue(response.getBody().asString(), RideResponseDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_GET_DTO);
    }

    @Test
    void createRide_ReturnsCreatedRideDto_AllMandatoryFieldsInRequestBody() throws Exception {
        stubForGettingDriverPassengerResponseDto(wireMockServer, objectMapper);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(RIDE_REQUEST_CREATE_DTO))
                .when()
                    .post(AppTestUtil.RIDE_PAGE_GET_POST_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.CREATED)
                    .extract()
                    .response();
        RideResponseDto actual = objectMapper.readValue(response.getBody().asString(), RideResponseDto.class);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_CREATE_DTO);
    }

    @Test
    void updateRide_ReturnsUpdatedRideDto_UpdatedDepartureAddressAndDestinationAddress() throws Exception {
        stubForGettingDriverPassengerResponseDto(wireMockServer, objectMapper);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(RIDE_REQUEST_UPDATE_DTO))
                .when()
                    .put(AppTestUtil.RIDE_UPDATE_ENDPOINT, RIDE_ID)
                .then()
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        RideResponseDto actual = objectMapper.readValue(response.getBody().asString(), RideResponseDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_UPDATE_DTO);
    }

    @Test
    void changeRideStatus_ReturnsUpdatedRideDto_UpdatedRideStatus() throws Exception {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(RIDE_STATUS_CHANGE_REQUEST_DTO))
                .when()
                    .patch(AppTestUtil.RIDE_CHANGE_RIDE_STATUS_ENDPOINT, RIDE_ID)
                .then()
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        RideResponseDto actual = objectMapper.readValue(response.getBody().asString(), RideResponseDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_AFTER_CHANGE_STATUS_DTO);
    }

    @Test
    void getPageRides_ReturnsPageWithRideDto_DefaultOffsetAndLimit() throws Exception {
        Response response = given()
                .when()
                    .get(AppTestUtil.RIDE_PAGE_GET_POST_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        ListContainerResponseDto<RideResponseDto> actual = objectMapper.readValue(
                response.getBody().asString(), new TypeReference<>() {
                });
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(INNER_IGNORING_FIELD_ONE, INNER_IGNORING_FIELD_TWO)
                .isEqualTo(PAGE_RIDE_RESPONSE_DTO);
    }

}
