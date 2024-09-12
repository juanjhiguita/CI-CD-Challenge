@active
Feature: Update the last created resource

  Scenario: Update the last created resource
    Given there are the last 15 resources
    When I find the latest created resource
    And I update all parameters of this resource
    Then the response should have status code 200
    Then the response body should match with the expected schema
    And the updated resource should be different from the old resource