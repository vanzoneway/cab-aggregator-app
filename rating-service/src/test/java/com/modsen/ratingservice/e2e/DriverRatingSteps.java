package com.modsen.ratingservice.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.modsen.ratingservice.e2e.E2ETestData.AVERAGE_RATING_POSTFIX;
import static com.modsen.ratingservice.e2e.E2ETestData.BASE_URL;
import static com.modsen.ratingservice.e2e.E2ETestData.ID_POSTFIX;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DriverRatingSteps {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private RatingRequestDto ratingRequestDto;

    private Response actual;

    @Given("the request body to create or update rating")
    public void setRatingRequestDtoForCreateEndpoint(String ratingRequest)
            throws Exception {
        ratingRequestDto = objectMapper.readValue(ratingRequest, RatingRequestDto.class);
    }

    @When("Create driver rating")
    public void createDriverRating() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(ratingRequestDto)
                .when()
                .post(BASE_URL);
    }

    @Then("the response status is {int}")
    public void responseStatusForCreateDriverRatingEndpoint(int responseStatus) {
        actual
                .then()
                    .statusCode(responseStatus);
    }

    @And("the response body contain the following data")
    public void responseBodyAfterCreatingDriverRatingContainTheFollowingData(String expected)
            throws Exception {
        assertThat(actual.as(RatingResponseDto.class))
                .isEqualTo(objectMapper.readValue(expected, RatingResponseDto.class));
    }

    @When("Get driver rating with id {int}")
    public void getDriverRatingWithId(int id) {
        actual = given()
                .when()
                    .get(BASE_URL + ID_POSTFIX, id);
    }

    @When("Update driver rating with id {int}")
    public void updateDriverRatingWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(ratingRequestDto)
                .when()
                    .put(BASE_URL + ID_POSTFIX, id);
    }

    @When("Delete driver rating with id {int}")
    public void deleteDriverRatingWithId(int id) {
        actual = given()
                .when()
                    .delete(BASE_URL + ID_POSTFIX, id);
    }

    @When("Get average driver rating with driver id {int}")
    public void getAverageDriverRatingWithId(int id) {
        actual = given()
                .when()
                    .get(BASE_URL + AVERAGE_RATING_POSTFIX, id);
    }

    @And("the response body after getting average driver rating contain the following data")
    public void responseBodyAfterGettingAverageDriverRatingContainTheFollowingData(String expected)
            throws Exception {
        assertThat(actual.as(AverageRatingResponseDto.class))
                .isEqualTo(objectMapper.readValue(expected, AverageRatingResponseDto.class));
    }

}
