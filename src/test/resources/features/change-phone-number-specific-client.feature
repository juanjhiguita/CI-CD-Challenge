@active @clients
@allure.label.layer:web
@allure.label.owner:juanjhiguita
@allure.label.page:/{org}/{repo}/labels
Feature: Update phone number of a specific client

  @smoke
  Scenario: Change the phone number of the first client named Laura
    Given there are at least 10 registered clients in the system
    And there is a client named "Laura"
    When I retrieve the details of the first client named "Laura"
    And I send a PUT request to update phone number of the client
    Then the response should have a status code of 200
    And the response should include the updated phone number
    And validates the response with client JSON schema
    And delete all the registered clients
