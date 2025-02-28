package com.modsen.passengerservice.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.IntegrationTestData;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.modsen.passengerservice.e2e.E2ETestData.ADMIN_AUTH_TOKEN_URL;
import static com.modsen.passengerservice.e2e.E2ETestData.BASE_URL;
import static com.modsen.passengerservice.e2e.E2ETestData.CLIENT_ID;
import static com.modsen.passengerservice.e2e.E2ETestData.CLIENT_SECRET;
import static com.modsen.passengerservice.e2e.E2ETestData.GRANT_TYPE;
import static com.modsen.passengerservice.e2e.E2ETestData.ID_FIELD;
import static com.modsen.passengerservice.e2e.E2ETestData.ID_POSTFIX;
import static com.modsen.passengerservice.e2e.E2ETestData.LIMIT_PARAM;
import static com.modsen.passengerservice.e2e.E2ETestData.OFFSET_PARAM;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PassengerServiceSteps {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Response actual;

    private PassengerDto passengerRequestDto;

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

    @Given("There is the following passenger details")
    public void preparePassengerDetails(String passengerRequest) throws Exception {
        passengerRequestDto = objectMapper.readValue(passengerRequest, PassengerDto.class);
    }

    @When("Creates the passenger")
    public void createPassenger() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(passengerRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .post(BASE_URL);
    }

    @Then("the response status is {int}")
    public void responseStatusShouldBeCreated(int expectedStatus) {
        actual
                .then()
                    .statusCode(expectedStatus);
    }

    @Then("the response body should contain the information about created passenger")
    public void responseBodyShouldContainTheInformationAboutCreatedPassenger(String passengerResponse)
            throws Exception {
        assertThat(actual.as(PassengerDto.class))
                .isEqualTo(objectMapper.readValue(passengerResponse, PassengerDto.class));
    }

    @When("Get a page with passengers with current offset {int} and limit {int}")
    public void getPageWithPassengersWithCurrentOffsetAndLimit(int offset, int limit) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .get(BASE_URL + OFFSET_PARAM + offset + LIMIT_PARAM + limit);

    }


    @And("the response body should contain the information about first three passengers")
    public void responseBodyShouldContainInformationAboutFirstThreePassengers(String passengerResponse)
            throws Exception {
        assertThat(actual.as(ListContainerResponseDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(objectMapper.readValue(passengerResponse, ListContainerResponseDto.class));
    }

    @When("Get passenger with id {int}")
    public void getPassengerWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .get(BASE_URL + ID_POSTFIX, id);
    }


    @And("the response body should contain the information about passenger")
    public void responseBodyShouldContainTheInformationAboutPassenger(String passengerResponse)
            throws Exception {
        assertThat(actual.as(PassengerDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(objectMapper.readValue(passengerResponse, PassengerDto.class));
    }

    @When("Update passenger with id {int}")
    public void updatePassengerWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(passengerRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .put(BASE_URL + ID_POSTFIX, id);
    }

    @And("the response body should contain the information about updated passenger")
    public void responseBodyShouldContainTheInformationAboutUpdatedPassenger(String passengerResponse)
            throws Exception {
        assertThat(actual.as(PassengerDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(objectMapper.readValue(passengerResponse, PassengerDto.class));
    }

    @When("Delete passenger with id {int}")
    public void deletePassengerWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .delete(BASE_URL + ID_POSTFIX, id);
    }

}
