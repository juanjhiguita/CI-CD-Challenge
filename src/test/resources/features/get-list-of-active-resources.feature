@active @resources
Feature: Get the list of active resources
  @smoke
  Scenario: Retrieve and update active resources
    Given there are at least 5 active resources
    When I get the list of all active resources
    Then the response should have status code 200
    And the response body should match with the expected resource list schema
    And I update all active resources to inactive


