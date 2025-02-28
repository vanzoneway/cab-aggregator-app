Feature: Rating service API

  Scenario: Get auth admin token
    When Get auth admin token

  Scenario: Create driver rating
    Given the request body to create or update rating
      """
        {
          "comment": "Great driver!",
          "rating" : "5",
          "rideId": 1
        }
      """
    When Create driver rating
    Then the response status is 201
    And the response body contain the following data
      """
        {
          "id": 29,
          "comment": "Great driver!",
          "userType": "DRIVER",
          "refUserId": 1,
          "rating": 5,
          "rideId": 1
        }
      """

  Scenario: Get driver rating
    When Get driver rating with id 5
    Then the response status is 200
    And the response body contain the following data
      """
        {
          "id": 5,
          "comment": "Great conversation and smooth driving.",
          "userType": "DRIVER",
          "refUserId": 3,
          "rating": 5,
          "rideId": 6
        }
      """

  Scenario: Update driver rating
    Given the request body to create or update rating
      """
        {
          "comment": "Great driverVVVV! Highly recommend!",
          "rating" : "5"
        }
      """
    When Update driver rating with id 11
    Then the response status is 200
    And the response body contain the following data
      """
        {
          "id": 11,
          "comment": "Great driverVVVV! Highly recommend!",
          "userType": "DRIVER",
          "refUserId": 2,
          "rating": 5,
          "rideId": 12
        }
      """

  Scenario: Delete driver rating
    When Delete driver rating with id 2
    Then the response status is 204

  Scenario:
    When Get average driver rating with driver id 1
    Then the response status is 200
    And the response body after getting average driver rating contain the following data
      """
        {
          "refUserId": 1,
          "averageRating": 4.0
        }
      """
