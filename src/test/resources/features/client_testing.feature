@active
Feature: Update phone number of a specific client


Scenario: Change the phone number of the first client named Laura
  Given there are at least 10 registered clients in the system
  And there is a client named "Laura"
  When I retrieve the details of the first client named "Laura"
  And I send a PUT request to update the client with ID "28"
  """
  {
    "name": "Laura",
    "lastName": "Gomez",
    "country": "Colombia",
    "city": "Bogota",
    "email": "lauragomez@email.com",
    "phone": "7676767676"
  }
  """
  Then the response should have a status code of 200
  And the response should include the updated phone number
  And the updated phone number should be different from the old phone number
  And the response should have the following details:
  | name  |lastName | country | city | email| phone   |
  | Laura | Gomez   | Colombia| Bogota | lauragomez@email.com| 7676767676  |
  And validates the response with client JSON schema