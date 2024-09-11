package com.testing.api.stepDefinitions;
import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import com.testing.api.utils.DataGenerator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);
    private Response response;
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Resource resource;
    private String resourceId;
    private List<Resource> resourceActiveList;

    @Given("there are at least {int} active resources")
    public void thereAreAtLeastActiveResources(int numberResourcesNeeded) {
        response = resourceRequest.getActiveResources();
        if(response.statusCode() == 404){
            resourceRequest.createDefaultResource();
        }
        response = resourceRequest.getActiveResources();
        Assert.assertEquals(200, response.statusCode());
        resourceActiveList = resourceRequest.getResourcesEntity(response);
        if(resourceActiveList.size() < numberResourcesNeeded){
            int index = 0;
            while(index < numberResourcesNeeded){
                Resource newResource = DataGenerator.generateResource();
                Response creationResponse = resourceRequest.createResource(newResource);
                Assert.assertEquals(201, creationResponse.statusCode());
                logger.info("Resource created with response: {}", creationResponse.jsonPath().prettify());
                index++;
            }
        }
    }

    @When("I get the list of all active resources")
    public void iGetTheListOfAllActiveResources() {
        response = resourceRequest.getActiveResources();
        logger.info(response.jsonPath()
                .prettify());
        resourceActiveList = resourceRequest.getResourcesEntity(response);
    }

    @Then("the response body should match with the expected schema")
    public void theResponseBodyShouldMatchWithTheExpectedSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }

    @And("I update all active resources to inactive")
    public void iUpdateAllActiveResourcesToInactive() {
        int index = 0;
        while(index < resourceActiveList.size()){
            resource = resourceActiveList.get(index);
            resourceId = resource.getId();
            resource.setActive(false);
            response = resourceRequest.updateActive(resourceId, resource);
            logger.info(response.jsonPath().prettify());
            index++;
        }
    }

    @Then("the response should have status code {int}")
    public void theResponseShouldHaveStatusCode(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info(response.jsonPath().prettify());
    }

}
