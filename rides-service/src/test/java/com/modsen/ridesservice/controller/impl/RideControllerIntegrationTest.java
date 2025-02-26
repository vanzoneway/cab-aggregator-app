package com.modsen.ridesservice.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ridesservice.IntegrationTestData;
import com.modsen.ridesservice.TestData;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;

import static com.modsen.ridesservice.IntegrationTestData.DRIVER_RESPONSE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.IGNORING_FIELD_ONE;
import static com.modsen.ridesservice.IntegrationTestData.IGNORING_FIELD_TWO;
import static com.modsen.ridesservice.IntegrationTestData.INNER_IGNORING_FIELD_ONE;
import static com.modsen.ridesservice.IntegrationTestData.INNER_IGNORING_FIELD_TWO;
import static com.modsen.ridesservice.IntegrationTestData.PAGE_RIDE_RESPONSE_DTO;
import static com.modsen.ridesservice.IntegrationTestData.PASSENGER_RESPONSE_DTO;
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
import static com.modsen.ridesservice.WireMockStubs.stubForGettingDriverResponseDto;
import static com.modsen.ridesservice.WireMockStubs.stubForGettingPassengerResponseDto;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9091)
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_DATA
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(value = "test")
class RideControllerIntegrationTest {

    @Autowired
    private CacheManager cacheManager;

    private static final String POSTGRESQL_IMAGE_NAME = "postgres:latest";
    private static final String REDIS_IMAGE_NAME = "redis:latest";

    private static final String EUREKA_CLIENT_ENABLED_PROPERTY = "eureka.client.enabled";
    private static final String BOOLEAN_PROPERTY_VALUE = "false";

    private static final String JWT_ISSUER_URI = "spring.security.oauth2.resourceserver.jwt.issuer-uri";
    private static final String JWT_ISSUER_URI_VALUE = "http://localhost:%d/realms/master";

    private static final int REDIS_EXPOSED_PORT = 6379;
    private static final int JWT_ISSUER_EXPOSED_PORT = 8080;

    @DynamicPropertySource
    private static void disableEureka(DynamicPropertyRegistry registry) {
        registry.add(EUREKA_CLIENT_ENABLED_PROPERTY, () -> BOOLEAN_PROPERTY_VALUE);
    }

    @ServiceConnection
    @Container
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName
            .parse(POSTGRESQL_IMAGE_NAME));

    @ServiceConnection
    @Container
    static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME))
            .withExposedPorts(REDIS_EXPOSED_PORT);

    @Container
    static final KeycloakContainer keycloakContainer = new KeycloakContainer();

    @DynamicPropertySource
    private static void configureJwtIssuerUri(DynamicPropertyRegistry registry) {
        registry.add(JWT_ISSUER_URI, () -> String.format(JWT_ISSUER_URI_VALUE,
                keycloakContainer.getMappedPort(JWT_ISSUER_EXPOSED_PORT)));
    }

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

    @AfterEach
    void clearCache() {
        cacheManager.getCacheNames().forEach(
                name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    @Test
    void getRideById_ReturnsRideDto_DatabaseContainsSuchRideId() {
        Response response = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .get(TestData.RIDE_GET_ENDPOINT)
                .then()
                    .contentType(ContentType.JSON)
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        RideResponseDto actual = response.as(RideResponseDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_GET_DTO);
    }

    @Test
    void createRide_ReturnsCreatedRideDto_AllMandatoryFieldsInRequestBody() throws Exception {
        stubForGettingPassengerResponseDto(wireMockServer, objectMapper, PASSENGER_RESPONSE_DTO);
        stubForGettingDriverResponseDto(wireMockServer, objectMapper, DRIVER_RESPONSE_DTO);
        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(RIDE_REQUEST_CREATE_DTO)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .post(TestData.RIDE_PAGE_GET_POST_ENDPOINT)
                .then()
                    .statusCode(HttpStatus.CREATED)
                    .extract()
                    .response();
        RideResponseDto actual = response.as(RideResponseDto.class);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_CREATE_DTO);
    }

    @Test
    void updateRide_ReturnsUpdatedRideDto_UpdatedDepartureAddressAndDestinationAddress() throws Exception {
        stubForGettingPassengerResponseDto(wireMockServer, objectMapper, PASSENGER_RESPONSE_DTO);
        stubForGettingDriverResponseDto(wireMockServer, objectMapper, DRIVER_RESPONSE_DTO);
        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(RIDE_REQUEST_UPDATE_DTO)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .put(TestData.RIDE_UPDATE_ENDPOINT, RIDE_ID)
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
    void changeRideStatus_ReturnsUpdatedRideDto_UpdatedRideStatus() {
        Response response = given()
                    .contentType(ContentType.JSON)
                    .body(RIDE_STATUS_CHANGE_REQUEST_DTO)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .patch(TestData.RIDE_CHANGE_RIDE_STATUS_ENDPOINT, RIDE_ID)
                .then()
                    .statusCode(HttpStatus.OK)
                    .extract()
                    .response();
        RideResponseDto actual = response.as(RideResponseDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(IGNORING_FIELD_ONE, IGNORING_FIELD_TWO)
                .isEqualTo(RIDE_RESPONSE_AFTER_CHANGE_STATUS_DTO);
    }

    @Test
    void getPageRides_ReturnsPageWithRideDto_DefaultOffsetAndLimit() throws Exception {
        Response response = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .get(TestData.RIDE_PAGE_GET_POST_ENDPOINT)
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
