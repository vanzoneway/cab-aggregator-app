Feature: Rides Service API

  Scenario: Get auth admin token
    When Get auth admin token

  Scenario: Create ride
    Given the request body contain the following data
      """
        {
          "passengerId": "15",
          "driverId" : "15",
          "departureAddress": "Minsk",
          "destinationAddress": "Brest"
        }
      """
    When Create ride
    Then the response status is 201
    And the response body contains the following data
      """
        {
          "id": 30,
          "driverId": 15,
          "passengerId": 15,
          "departureAddress": "Minsk",
          "destinationAddress": "Brest",
          "rideStatus": "CREATED"
        }
      """

  Scenario: Update ride status:
    Given the request body to update status contain the following data
      """
        {
          "rideStatus": "CANCELED"
        }
      """
    When Update ride status with id 1
    Then the response status is 200
    And the response body contains the following data
      """
        {
          "id": 1,
          "driverId": 1,
          "passengerId": 1,
          "departureAddress": "123 Main St",
          "destinationAddress": "456 Elm St",
          "rideStatus": "CANCELED"
        }
      """

  Scenario: Update ride
    Given the request body contain the following data
      """
        {
          "driverId": "1"
        }
      """
    When Update ride with id 2
    Then the response status is 200
    And the response body contains the following data
      """
        {
          "id": 2,
          "driverId": 1,
          "passengerId": 2,
          "departureAddress": "789 Oak St",
          "destinationAddress": "321 Pine St",
          "rideStatus": "ACCEPTED"
        }
      """

  Scenario: Get Ride:
    When Get ride with id 11
    Then the response status is 200
    And the response body contains the following data
    """
      {
        "id": 11,
        "driverId": 1,
        "passengerId": 4,
        "departureAddress": "654 Spruce St",
        "destinationAddress": "456 Birch St",
        "rideStatus": "COMPLETED"
      }
    """

