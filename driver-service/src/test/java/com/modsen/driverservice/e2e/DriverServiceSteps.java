package com.modsen.driverservice.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.driverservice.IntegrationTestData;
import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.modsen.driverservice.e2e.E2ETestData.ADMIN_AUTH_TOKEN_URL;
import static com.modsen.driverservice.e2e.E2ETestData.CAR_BASE_URL;
import static com.modsen.driverservice.e2e.E2ETestData.CLIENT_ID;
import static com.modsen.driverservice.e2e.E2ETestData.CLIENT_SECRET;
import static com.modsen.driverservice.e2e.E2ETestData.CREATE_CAR_POSTFIX;
import static com.modsen.driverservice.e2e.E2ETestData.DRIVER_BASE_URL;
import static com.modsen.driverservice.e2e.E2ETestData.GRANT_TYPE;
import static com.modsen.driverservice.e2e.E2ETestData.ID_FIELD;
import static com.modsen.driverservice.e2e.E2ETestData.ID_POSTFIX;
import static com.modsen.driverservice.e2e.E2ETestData.UPDATE_CAR_POSTFIX;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class DriverServiceSteps {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DriverDto driverRequestDto;
    private CarDto carRequestDto;
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

    @Given("the request body to create or update car")
    public void requestBodyToCreateOrUpdateCar(String requestBody)
            throws Exception {
        carRequestDto = objectMapper.readValue(requestBody, CarDto.class);
    }

    @When("Create car to driver with id {int}")
    public void createCarToDriverWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(carRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .post(CAR_BASE_URL + CREATE_CAR_POSTFIX, id);
    }

    @Then("the response status is {int}")
    public void responseStatusIs(int responseStatus) {
        actual
                .then()
                        .statusCode(responseStatus);
    }

    @And("the response body contain the following car data")
    public void responseBodyContainTheFollowingCarData(String expected)
            throws Exception {
        assertThat(actual.as(CarDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(objectMapper.readValue(expected, CarDto.class));
    }

    @When("Update car with id {int} and will relate to driver with id {int}")
    public void updateCarWithIdAndWillRelateToDriverWithId(int carId, int driverId) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(carRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                   .put(CAR_BASE_URL + UPDATE_CAR_POSTFIX, carId, driverId);


    }

    @When("Get car with id {int}")
    public void getCarWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .get(CAR_BASE_URL + ID_POSTFIX, id);
    }

    @When("Delete car with id {int}")
    public void deleteCarWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .delete(CAR_BASE_URL + ID_POSTFIX, id);
    }

    @Given("the request body to create or update driver")
    public void requestBodyToCreateOrUpdateDriver(String requestBody)
            throws Exception {
        driverRequestDto = objectMapper.readValue(requestBody, DriverDto.class);
    }

    @When("Create driver")
    public void createDriver() {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(driverRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .post(DRIVER_BASE_URL);
    }

    @And("the response body contain the following driver data")
    public void responseBodyContainTheFollowingDriverData(String expected)
            throws Exception {
        assertThat(actual.as(DriverDto.class))
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(objectMapper.readValue(expected, DriverDto.class));
    }

    @When("Update driver with id {int}")
    public void updateDriverWithId(int id) {
        actual = given()
                    .contentType(ContentType.JSON)
                    .body(driverRequestDto)
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .put(DRIVER_BASE_URL + ID_POSTFIX, id);
    }

    @When("Delete driver with id {int}")
    public void deleteDriverWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .delete(DRIVER_BASE_URL + ID_POSTFIX, id);
    }

    @When("Get driver with id {int}")
    public void getDriverWithId(int id) {
        actual = given()
                    .header(IntegrationTestData.AUTHORIZATION,
                        IntegrationTestData.BEARER + adminKeycloakTokenResponseDto.accessToken())
                .when()
                    .get(DRIVER_BASE_URL + ID_POSTFIX, id);
    }

}
