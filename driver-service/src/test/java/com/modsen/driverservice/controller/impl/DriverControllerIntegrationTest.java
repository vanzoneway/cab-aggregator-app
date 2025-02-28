package com.modsen.driverservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.IntegrationTestData;
import com.modsen.driverservice.TestData;
import com.modsen.driverservice.kafka.consumer.averagerating.AverageRatingListener;
import dasniko.testcontainers.keycloak.KeycloakContainer;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.driverservice.IntegrationTestData.DRIVER_CAR_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_CREATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_CREATE_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_PAGE_GET_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_UPDATE_REQUEST_DTO;
import static com.modsen.driverservice.IntegrationTestData.DRIVER_UPDATE_RESPONSE_DTO;
import static com.modsen.driverservice.IntegrationTestData.ID;
import static com.modsen.driverservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.driverservice.IntegrationTestData.SQL_INSERT_CAR_DRIVER;
import static com.modsen.driverservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({AverageRatingListener.class})
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_CAR_DRIVER
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(value = "test")
class DriverControllerIntegrationTest {

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

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void createDriver_ReturnsCreatedDriverDto_AllMandatoryParamsInRequestBody() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(DRIVER_CREATE_REQUEST_DTO)
                    .header(AUTHORIZATION, prepareAuthorizationHeader())
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
                    .header(AUTHORIZATION, prepareAuthorizationHeader())
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
                    .body(DRIVER_UPDATE_REQUEST_DTO)
                    .header(AUTHORIZATION, prepareAuthorizationHeader())
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
                    .header(AUTHORIZATION, prepareAuthorizationHeader())
                .when()
                    .delete(TestData.DRIVER_DELETE_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getDriverWithCars_ReturnsDriverCarDto_DatabaseContainsSuchDriverId() throws Exception {
        given()
                    .header(AUTHORIZATION, prepareAuthorizationHeader())
                .when()
                    .get(TestData.DRIVER_CARS_ENDPOINT, ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(DRIVER_CAR_RESPONSE_DTO)));
    }

    private String prepareAuthorizationHeader() {
        return IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                .tokenManager()
                .getAccessToken()
                .getToken();
    }

}
