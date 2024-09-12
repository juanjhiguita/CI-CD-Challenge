@inactive @clients
Feature: Update and delete a new client
  @smoke
  Scenario: Create, update, and delete a new client
    When I create a new client
    And I find the newly created client
    And I send a PATCH request to update only the phone number of the new client
    """
    {
      "phone": "5432109876"
    }
    """
    Then the response should have a status code of 200
    And the response should include the updated phone number
    And validate that response body have all the data
    And validates the response with client JSON schema
    And I send a DELETE request to delete the new client
    And the response should have a status code of 200


