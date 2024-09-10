package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private Client client;
    private String oldPhoneNumber;

    @Given("there are at least 10 registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());
        List<Client> clientList = clientRequest.getClientsEntity(response);
        Assert.assertTrue(clientList.size() >= 10);
        // TODO: HACER LA GENERACION DE LOS CLIENTES SI HAY MENOS DE 10
        logger.info("Empieza");
    }

    @Given("there is a client named {string}")
    public void thereIsAClientNamedLaura(String name) {
        response = clientRequest.getClientsByName(name);
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);
        if (clientList.isEmpty()) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }


    @When("I retrieve the details of the first client named {string}")
    public void iRetrieveTheDetailsOfTheFirstClientNamed(String name) {
        List<Client> clientList = clientRequest.getClientsEntity(response);
        client = clientList.get(0);
        oldPhoneNumber = client.getPhone();
        logger.info(client);
    }

    @When("I send a PUT request to update the client with ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithID(String clientId, String requestBody) {
        Client clientToUpdate = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updatePhoneNumber(clientId, clientToUpdate);
        logger.info(response.jsonPath());
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
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
        logger.info("Termina");
    }
}
