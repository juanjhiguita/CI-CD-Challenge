@active
Feature: Get the list of active resources

  Scenario: Retrieve and update active resources
    Given there are at least 5 active resources
    When I get the list of all active resources
    And I update all active resources to inactive
    And the response body should match the expected schema

