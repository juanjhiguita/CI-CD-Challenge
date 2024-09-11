package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private Response response;
    private final ClientRequest clientRequest = new ClientRequest();
    private Client client;
    private String clientId;
    private String oldPhoneNumber;

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info(response.jsonPath().prettify());
    }

    @Given("there are at least 10 registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());
        List<Client> clientList = clientRequest.getClientsEntity(response);
        Faker faker = new Faker();
        if(clientList.size() < 10){
            int index = 0;
            while(index < 10){
                String name = faker.name().firstName();
                String lastName = faker.name().lastName();
                String country = faker.country().name();
                String city = faker.address().cityName();
                String email = faker.internet().emailAddress();
                String phone = faker.phoneNumber().phoneNumber();

                Client newClient = new Client();
                newClient.setName(name);
                newClient.setLastName(lastName);
                newClient.setCountry(country);
                newClient.setCity(city);
                newClient.setEmail(email);
                newClient.setPhone(phone);

                Response creationResponse = clientRequest.createClient(newClient);
                Assert.assertEquals(201, creationResponse.statusCode());
                logger.info("Client created with response: {}", creationResponse.jsonPath().prettify());
                index++;
            }
        }
    }

    @Given("there is a client named {string}")
    public void thereIsAClientNamedLaura(String name) {
        try{
            response = clientRequest.getClientsByName(name);
            List<Client> clientList = clientRequest.getClientsEntity(response);
            if (clientList.isEmpty()) {
                response = clientRequest.createDefaultClient();
                logger.info(response.statusCode());
                Assert.assertEquals(201, response.statusCode());
            }
            response = clientRequest.getClientsByName(name);
            logger.info(response.jsonPath()
                    .prettify());
            Assert.assertEquals(200, response.statusCode());

        }catch (JsonPathException jsonPathException){
            response = clientRequest.createDefaultClient();
            logger.info(response.jsonPath()
                    .prettify());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @When("I retrieve the details of the first client named {string}")
    public void iRetrieveTheDetailsOfTheFirstClientNamed(String name) {
        response = clientRequest.getClientsByName(name);
        List<Client> clientList = clientRequest.getClientsEntity(response);
        client = clientList.get(0);
        clientId = client.getId();
        oldPhoneNumber = client.getPhone();
        logger.info(client);
    }

    @When("I send a PUT request to update the client")
    public void iSendAPUTRequestToUpdateTheClient(String requestBody) {
        Client clientToUpdate = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updatePhoneNumber(clientId, clientToUpdate);
        logger.info(response.jsonPath().prettify());
    }

    @Then("the response should include the updated phone number")
    public void theResponseShouldIncludeTheUpdatedPhoneNumber() {
        Assert.assertTrue(clientRequest.includePhoneNumber(response));
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response.jsonPath().prettify());
        Map<String, String> expectedDataMap = expectedData.asMaps()
                .get(0);

        Assert.assertEquals(expectedDataMap.get("name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("lastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("city"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("email"), client.getEmail());
        Assert.assertEquals(expectedDataMap.get("phone"), client.getPhone());
    }

    @Then("validates the response with client JSON schema")
    public void validatesTheResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("the updated phone number should be different from the old phone number")
    public void theUpdatedPhoneNumberShouldBeDifferentFromTheOldPhoneNumber() {
        client = clientRequest.getClientEntity(response.jsonPath().prettify());
        Assert.assertNotEquals(oldPhoneNumber, client.getPhone());
        logger.info("Finish");
    }

    @When("I create a new client")
    public void iCreateANewClient() {
        response = clientRequest.createDefaultClient();
        client = clientRequest.getClientEntity(response.jsonPath().prettify());
        clientId = client.getId();
        Assert.assertEquals(201, response.statusCode());
        logger.info("Client created with response: {}", response.jsonPath().prettify());
    }

    @And("I find the newly created client")
    public void iFindTheNewlyCreatedClient() {
        response = clientRequest.getClientById(clientId);
        Assert.assertEquals(200, response.statusCode());
    }

    @And("I send a PATCH request to update only the phone number of the new client")
    public void iSendAPATCHRequestToUpdateOnlyThePhoneNumberOfTheNewClient(String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateOnlyPhoneNumber(clientId, requestBody);
        logger.info(response.jsonPath());
    }


    @And("I send a DELETE request to delete the new client")
    public void iSendADELETERequestToDeleteTheNewClient() {
        response = clientRequest.deleteClient(clientId);
    }

    @And("validate that response body have all the data")
    public void validateThatResponseBodyHaveAllTheData() {
        Assert.assertTrue(clientRequest.isUpdateResponseBodyValid(response));
    }


}
