package com.modsen.ratingservice.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ratingservice.IntegrationTestData;
import com.modsen.ratingservice.TestData;
import com.modsen.ratingservice.kafka.KafkaProducerConfig;
import com.modsen.ratingservice.kafka.producer.AverageRatingSender;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
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

import java.util.Objects;

import static com.modsen.ratingservice.IntegrationTestData.AVERAGE_RATING_RESPONSE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.RATING_ID;
import static com.modsen.ratingservice.IntegrationTestData.RATING_REQUEST_CREATE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.RATING_REQUEST_UPDATE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.RATING_RESPONSE_CREATE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.RATING_RESPONSE_GET_DTO;
import static com.modsen.ratingservice.IntegrationTestData.RATING_RESPONSE_UPDATE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.REF_USER_ID;
import static com.modsen.ratingservice.IntegrationTestData.RIDE_RESPONSE_DTO;
import static com.modsen.ratingservice.IntegrationTestData.SQL_DELETE_ALL_DATA;
import static com.modsen.ratingservice.IntegrationTestData.SQL_INSERT_DATA;
import static com.modsen.ratingservice.IntegrationTestData.SQL_RESTART_SEQUENCES;
import static com.modsen.ratingservice.WireMockStubs.stubForGettingRideResponseDto;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({AverageRatingSender.class, KafkaProducerConfig.class})
@AutoConfigureWireMock(port = 9090)
@Sql(statements = {
        SQL_DELETE_ALL_DATA,
        SQL_RESTART_SEQUENCES,
        SQL_INSERT_DATA
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(value = "test")
class DriverRatingControllerIntegrationTest {

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
    void createDriverRating_ReturnsCreatedDriverDto_AllMandatoryFieldInRequestBody() throws Exception {
        stubForGettingRideResponseDto(wireMockServer, objectMapper, RIDE_RESPONSE_DTO);
        given()
                    .contentType(ContentType.JSON)
                    .body(RATING_REQUEST_CREATE_DTO)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                        .post(TestData.DRIVER_RATING_ENDPOINT)
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                            .contentType(ContentType.JSON)
                            .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_CREATE_DTO)));
    }

    @Test
    void getAverageRating_ReturnsAverageRatingResponseDto_SuchRefUserIdExistsInDatabase() throws Exception {
        given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .get(TestData.DRIVER_RATING_AVERAGE_ENDPOINT, REF_USER_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(AVERAGE_RATING_RESPONSE_DTO)));
    }

    @Test
    void updateDriverRating_ReturnsUpdatedDriverRatingDto_UpdatedRatingAndCommentFields() throws Exception {
        given()
                    .contentType(ContentType.JSON)
                    .body(RATING_REQUEST_UPDATE_DTO)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .put(TestData.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_UPDATE_DTO)));
    }

    @Test
    void safeDeleteDriverRating_ReturnsNoContentStatusCode_DatabaseContainsSuchRatingId() {
        given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .delete(TestData.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getDriverRating_ReturnsDriverRatingDto_DatabaseContainsSuchRatingId() throws Exception {
        given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + keycloakContainer.getKeycloakAdminClient()
                                .tokenManager()
                                .getAccessToken()
                                .getToken())
                .when()
                    .get(TestData.DRIVER_RATING_UPDATE_DELETE_ENDPOINT, RATING_ID)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body(equalTo(objectMapper.writeValueAsString(RATING_RESPONSE_GET_DTO)));
    }

}
