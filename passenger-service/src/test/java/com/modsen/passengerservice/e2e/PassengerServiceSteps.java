package com.modsen.passengerservice.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.modsen.passengerservice.e2e.E2ETestData.BASE_URL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PassengerServiceSteps {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Response actual;

    private PassengerDto passengerRequestDto;

    @Given("There is the following passenger details")
    public void preparePassengerDetails(String passengerRequest) throws Exception {
        passengerRequestDto = objectMapper.readValue(passengerRequest, PassengerDto.class);
    }

    @When("Creates the passenger")
    public void createPassenger() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(passengerRequestDto)
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
                .when()
                    .get(BASE_URL + "?offset=" + offset + "&limit=" + limit);

    }


    @And("the response body should contain the information about first three passengers")
    public void responseBodyShouldContainInformationAboutFirstThreePassengers(String passengerResponse)
            throws Exception {
        assertThat(actual.as(ListContainerResponseDto.class))
                .isEqualTo(objectMapper.readValue(passengerResponse, ListContainerResponseDto.class));
    }

    @When("Get passenger with id {int}")
    public void getPassengerWithId(int id) {
        actual = given()
                .when()
                    .get(BASE_URL + "/{id}", id);
    }


    @And("the response body should contain the information about passenger")
    public void responseBodyShouldContainTheInformationAboutPassenger(String passengerResponse)
            throws Exception {
        assertThat(actual.as(PassengerDto.class))
                .isEqualTo(objectMapper.readValue(passengerResponse, PassengerDto.class));
    }

    @When("Update passenger with id {int}")
    public void updatePassengerWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(passengerRequestDto)
                .when()
                    .put(BASE_URL + "/{id}", id);
    }

    @And("the response body should contain the information about updated passenger")
    public void responseBodyShouldContainTheInformationAboutUpdatedPassenger(String passengerResponse)
            throws Exception {
        assertThat(actual.as(PassengerDto.class))
                .isEqualTo(objectMapper.readValue(passengerResponse, PassengerDto.class));
    }

    @When("Delete passenger with id {int}")
    public void deletePassengerWithId(int id) {
        actual = given()
                .when()
                    .delete(BASE_URL + "/{id}", id);
    }

}
