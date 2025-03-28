Feature: Driver service API

  Scenario: Get auth admin token
    When Get auth admin token

  Scenario: Create car to driver
    Given the request body to create or update car
      """
        {
          "brand": "Toyota",
          "color": "Green",
          "year": 2020,
          "model": "Toyota Camry",
          "number": "CDE870"
        }

      """
    When Create car to driver with id 3
    Then the response status is 201
    And the response body contain the following car data
      """
        {
          "id": 41,
          "brand": "Toyota",
          "color": "Green",
          "number": "CDE870",
          "model": "Toyota Camry",
          "year": 2020,
          "deleted": false
        }
      """

  Scenario: Update car
    Given the request body to create or update car
      """
        {
          "color": "Pink",
          "number": "CDE694"
        }
      """
    When Update car with id 11 and will relate to driver with id 3
    Then the response status is 200
    And the response body contain the following car data
      """
        {
          "id": 11,
          "brand": "Toyota",
          "color": "Pink",
          "number": "CDE694",
          "model": "Toyota RAV4",
          "year": 2021,
          "deleted": false
        }
      """

  Scenario: Get car
    When Get car with id 1
    Then the response status is 200
    And the response body contain the following car data
      """
        {
          "id": 1,
          "brand": "Toyota",
          "color": "Red",
          "number": "ABC123",
          "model": "Toyota Camry",
          "year": 2020,
          "deleted": false
        }
      """

  Scenario: Delete car
    When Delete car with id 10
    Then the response status is 204

  Scenario: Create driver
    Given the request body to create or update driver
      """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johnddoe@eоxample.com",
          "phone": "+1234567503",
          "gender": "Male"
        }
      """
    When Create driver
    Then the response status is 201
    And the response body contain the following driver data
      """
        {
          "id": 21,
          "firstName": "John",
          "lastName": "Doe",
          "email": "johnddoe@eоxample.com",
          "phone": "+1234567503",
          "gender": "Male",
          "averageRating": null,
          "deleted": false
        }
      """

  Scenario: Update driver
    Given the request body to create or update driver
      """
        {
          "phone": "+123456789"
        }
      """
    When Update driver with id 3
    Then the response status is 200
    And the response body contain the following driver data
      """
        {
          "id": 3,
          "firstName": "Bob",
          "lastName": "Johnson",
          "email": "bobjohnson@example.com",
          "phone": "+123456789",
          "gender": "Male",
          "averageRating": null,
          "deleted": false
        }
      """

  Scenario: Delete driver
    When Delete driver with id 10
    Then the response status is 204

  Scenario: Get driver:
    When Get driver with id 2
    Then the response status is 200
    And the response body contain the following driver data
      """
        {
          "id": 2,
          "firstName": "Jane",
          "lastName": "Smith",
          "email": "janesmith@example.com",
          "phone": "+1234567891",
          "gender": "Female",
          "averageRating": null,
          "deleted": false
        }
      """