package com.testing.api.stepDefinitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import com.testing.api.utils.DataGenerator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private Response response;
    private final ClientRequest clientRequest = new ClientRequest();
    private Client client;
    private String clientId;
    private String oldPhoneNumber;

    @Given("there are at least {int} registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem(int numberResourcesNeeded) {
        response = clientRequest.getClients();
        if(response.statusCode() == 404){
            response = clientRequest.createDefaultClient();
            Assert.assertEquals(201, response.statusCode());
            response = clientRequest.getClients();
        }
        Assert.assertEquals(200, response.statusCode());
        List<Client> clientList = clientRequest.getClientsEntity(response);
        if(clientList.size() < numberResourcesNeeded){
            DataGenerator.generateClients(numberResourcesNeeded);
        }
    }

    @Given("there is a client named {string}")
    public void thereIsAClientNamedLaura(String name) {
        try{
            response = clientRequest.getClientsByName(name);
            List<Client> clientList = clientRequest.getClientsEntity(response);
            if (clientList.isEmpty()) {
                response = clientRequest.createDefaultClient();
                logger.info("A default client was created {}", response.jsonPath().prettify());
                Assert.assertEquals(201, response.statusCode());
            }
            response = clientRequest.getClientsByName(name);
            Assert.assertEquals(200, response.statusCode());
            logger.info("List of clients named {}", response.jsonPath().prettify());
        }catch (JsonPathException jsonPathException){
            response = clientRequest.createDefaultClient();
            Assert.assertEquals(201, response.statusCode());
            logger.info(response.jsonPath()
                    .prettify());
        }
    }

    @When("I retrieve the details of the first client named {string}")
    public void iRetrieveTheDetailsOfTheFirstClientNamed(String name) {
        response = clientRequest.getClientsByName(name);
        List<Client> clientList = clientRequest.getClientsEntity(response);
        client = clientList.get(0);
        clientId = client.getId();
        oldPhoneNumber = client.getPhone();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(client);
        logger.info("Response to retrieve the defailts of the fistt client named \n" + jsonOutput);
    }

    @When("I send a PUT request to update phone number of the client")
    public void iSendAPUTRequestToUpdatePhoneNumberOfTheClient() {
        client.setPhone(DataGenerator.generatePhoneNumber());
        response = clientRequest.updatePhoneNumber(clientId, client);
        logger.info("Response to update phoneNumber {}", response.jsonPath().prettify());
    }

    @Then("the response should include the updated phone number")
    public void theResponseShouldIncludeTheUpdatedPhoneNumber() {
        Assert.assertTrue(clientRequest.includePhoneNumber(response));
        logger.info("The response include the updated phone number");
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info("The status code {}", response.statusCode());
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
        logger.info("The updated phone number is different from the old");
    }

    @When("I create a new client")
    public void iCreateANewClient() {
        response = clientRequest.createDefaultClient();
        client = clientRequest.getClientEntity(response.jsonPath().prettify());
        clientId = client.getId();
        Assert.assertEquals(201, response.statusCode());
        logger.info("Client created with response: {}", response.jsonPath().prettify());
    }

    @When("I find the newly created client")
    public void iFindTheNewlyCreatedClient() {
        response = clientRequest.getClientById(clientId);
        Assert.assertEquals(200, response.statusCode());
        logger.info("The newly client was found: {}", response.jsonPath().prettify());

    }

    @When("I send a PATCH request to update only the phone number of the new client")
    public void iSendAPATCHRequestToUpdateOnlyThePhoneNumberOfTheNewClient(String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateOnlyPhoneNumber(clientId, requestBody);
        logger.info("Response to update only phoneNumber {}", response.jsonPath().prettify());
    }


    @Then("I send a DELETE request to delete the new client")
    public void iSendADELETERequestToDeleteTheNewClient() {
        response = clientRequest.deleteClient(clientId);
        logger.info("The new client was deleted {}", response.jsonPath().prettify());
    }

    @Then("validate that response body have all the data")
    public void validateThatResponseBodyHaveAllTheData() {
        Assert.assertTrue(clientRequest.isUpdateResponseBodyValid(response));
        logger.info("The response body have all the data");
    }


    @Then("delete all the registered clients")
    public void deleteAllTheRegisteredClients() {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        int index = 0;
        while(index <= clientList.size()){
            response = clientRequest.deleteClient(String.valueOf(index));
            index++;
        }
        logger.info("All the clients were deleted");
    }
}
