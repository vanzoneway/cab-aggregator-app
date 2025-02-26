Feature: Passenger Service API

  Scenario: Get auth admin token
    When Get auth admin token

  Scenario: Create a new passenger
    Given There is the following passenger details
    """
      {
        "firstName": "John",
        "lastName": "Lock",
        "email": "john.lock1@example.com",
        "phone": "+1234567890"
      }
    """
    When Creates the passenger
    Then the response status is 201
    And the response body should contain the information about created passenger
    """
      {
        "id": 69,
        "firstName": "John",
        "lastName": "Lock",
        "email": "john.lock1@example.com",
        "phone": "+1234567890",
        "deleted": false,
        "averageRating": null
      }
    """

  Scenario: Get paginated passengers:
    When Get a page with passengers with current offset 1 and limit 3
    Then the response status is 200
    And the response body should contain the information about first three passengers
    """
      {
        "currentOffset": 1,
        "currentLimit": 3,
        "totalPages": 23,
        "totalElements": 69,
        "sort": "UNSORTED",
        "values": [
            {
                "id": 4,
                "firstName": "Dmitry",
                "lastName": "Kuznetsov",
                "email": "dmitry4@example.com",
                "phone": "+4930123457",
                "averageRating": null,
                "deleted": false
            },
            {
                "id": 5,
                "firstName": "Elena",
                "lastName": "Sidorova",
                "email": "elena5@example.com",
                "phone": "+74951234568",
                "averageRating": null,
                "deleted": false
            },
            {
                "id": 6,
                "firstName": "Sergey",
                "lastName": "Lebedev",
                "email": "sergey6@example.com",
                "phone": "+331444556678",
                "averageRating": null,
                "deleted": false
            }
        ]
      }
    """

  Scenario: Get passenger:
    When Get passenger with id 1
    Then the response status is 200
    And the response body should contain the information about passenger
    """
      {
        "id": 1,
        "firstName": "Ivan",
        "lastName": "Ivanov",
        "email": "ivan1@example.com",
        "phone": "+375336402547",
        "averageRating": null,
        "deleted": false
      }
    """

  Scenario: Update passenger:
    Given There is the following passenger details
    """
      {
        "phone": "+34911111111"
      }
    """
    When Update passenger with id 9
    Then the response status is 200
    And the response body should contain the information about updated passenger
      """
        {
          "id": 9,
          "firstName": "Pavel",
          "lastName": "Nikolaev",
          "email": "pavel9@example.com",
          "phone": "+34911111111",
          "averageRating": null,
          "deleted": false
        }
      """


  Scenario: Delete passenger:
    When Delete passenger with id 1
    Then the response status is 204
