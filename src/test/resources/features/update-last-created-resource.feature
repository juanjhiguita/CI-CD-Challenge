@active @resources
@allure.label.layer:web
@allure.label.owner:juanjhiguita
@allure.label.page:/{org}/{repo}/labels
Feature: Update the last created resource
  @smoke
  Scenario: Update the last created resource
    Given there are the last 15 resources
    When I find the latest created resource
    And I update all parameters of this resource
    Then the response should have status code 200
    And the response body should match with the expected resource schema
    And the updated resource should be different from the old resource