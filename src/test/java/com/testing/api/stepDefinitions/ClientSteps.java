package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private Client client;

    //TODO: ESTO HAY QUE ARREGRARLO
    @Given("there are at least 10 registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());
        List<Client> clientList = clientRequest.getClientsEntity(response);
        if (clientList.isEmpty() || clientList.size() < 10) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
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

    @When("I retrieve the details of the first client named Laura")
    public void iRetrieveTheDetailsOfTheFirstClientNamedLaura() {
        List<Client> clientList = clientRequest.getClientsEntity(response);
        client = clientList.get(0);
        logger.info(client);
    }

    @When("I send a PUT request to update the client with ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithID(String clientId, String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updatePhoneNumber(client, clientId);
        logger.info(response.jsonPath().prettify());
        logger.info("Empieza");
    }


    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should include the updated phone number")
    public void theResponseShouldIncludeTheUpdatedPhoneNumber() {
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {

    }

    @Then("validates the response with client JSON schema")
    public void validatesTheResponseWithClientJSONSchema() {

    }

    @Then("the updated phone number should be different from the saved phone number")
    public void theUpdatedPhoneNumberShouldBeDifferentFromTheSavedPhoneNumber() {
        logger.info("Termina");
    }


}
