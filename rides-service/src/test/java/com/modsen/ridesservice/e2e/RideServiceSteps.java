package com.modsen.ridesservice.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ridesservice.IntegrationTestData;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.modsen.ridesservice.e2e.E2ETestData.ADMIN_AUTH_TOKEN_URL;
import static com.modsen.ridesservice.e2e.E2ETestData.BASE_URL;
import static com.modsen.ridesservice.e2e.E2ETestData.CLIENT_ID;
import static com.modsen.ridesservice.e2e.E2ETestData.CLIENT_SECRET;
import static com.modsen.ridesservice.e2e.E2ETestData.COST_FIELD;
import static com.modsen.ridesservice.e2e.E2ETestData.GRANT_TYPE;
import static com.modsen.ridesservice.e2e.E2ETestData.ID_FIELD;
import static com.modsen.ridesservice.e2e.E2ETestData.ORDER_DATE_TIME_FIELD;
import static com.modsen.ridesservice.e2e.E2ETestData.RIDE_ID_POSTFIX;
import static com.modsen.ridesservice.e2e.E2ETestData.UPDATE_RIDE_STATUS_POSTFIX;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RideServiceSteps {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private RideStatusRequestDto rideStatusRequestDto;

    private RideRequestDto rideRequestDto;

    private Response actual;

    private static AdminKeycloakTokenResponseDto adminKeycloakTokenResponseDto;

    @When("Get auth admin token")
    public void getAuthAdminToken() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(new SignInAdminDto(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET))
                .when()
                    .post(ADMIN_AUTH_TOKEN_URL);
        adminKeycloakTokenResponseDto = actual.as(AdminKeycloakTokenResponseDto.class);
    }

    @Given("the request body contain the following data")
    public void requestBodyContainTheFollowingData(String requestBody)
            throws Exception {
        rideRequestDto = objectMapper.readValue(requestBody, RideRequestDto.class);
    }

    @When("Create ride")
    public void createRide() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(rideRequestDto)
                    .header(AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .post(BASE_URL);
    }

    @Then("the response status is {int}")
    public void responseStatusIs(int responseStatus) {
        actual
                .then()
                    .statusCode(responseStatus);
    }

    @And("the response body contains the following data")
    public void responseBodyContainsTheFollowingData(String expected)
            throws Exception {
        assertThat(actual.as(RideResponseDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ORDER_DATE_TIME_FIELD, COST_FIELD, ID_FIELD)
                .isEqualTo(objectMapper.readValue(expected, RideResponseDto.class));
    }

    @Given("the request body to update status contain the following data")
    public void requestBodyToUpdateStatusContainTheFollowingData(String requestBody)
            throws Exception {
        rideStatusRequestDto = objectMapper.readValue(requestBody, RideStatusRequestDto.class);
    }

    @When("Update ride status with id {int}")
    public void updateRideStatusWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(rideStatusRequestDto)
                    .header(AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .patch(BASE_URL + UPDATE_RIDE_STATUS_POSTFIX, id);
    }

    @When("Update ride with id {int}")
    public void updateRideWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(rideRequestDto)
                    .header(AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .put(BASE_URL + RIDE_ID_POSTFIX, id);
    }

    @When("Get ride with id {int}")
    public void getRideWithId(int id) {
        actual = given()
                    .header(AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .get(BASE_URL + RIDE_ID_POSTFIX, id);
    }

}
