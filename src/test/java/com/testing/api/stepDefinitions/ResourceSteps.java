package com.testing.api.stepDefinitions;
import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import com.testing.api.utils.Constants;
import com.testing.api.utils.DataGenerator;
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
import java.util.Map;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);
    private Response response;
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Resource resource;
    private String resourceId;
    private Resource lastResource;
    private List<Resource> resourceActiveList;
    private List<Resource> resourceList;


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
        String path = "schemas/resourceSchema.json";
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
        logger.info(response.statusCode());
    }

    @Given("there are the last {int} resources")
    public void thereAreTheLastResources(int numberResourcesNeeded) {
        response = resourceRequest.getResources();
        if(response.statusCode() == 404){
            resourceRequest.createDefaultResource();
            response = resourceRequest.getResources();
        }
        Assert.assertEquals(200, response.statusCode());
        resourceList = resourceRequest.getResourcesEntity(response);
        if(resourceList.size() < numberResourcesNeeded){
            int index = 1;
            while(index < numberResourcesNeeded){
                Resource newResource = DataGenerator.generateResource();
                Response creationResponse = resourceRequest.createResource(newResource);
                Assert.assertEquals(201, creationResponse.statusCode());
                logger.info("Resource created with response: {}", creationResponse.jsonPath().prettify());
                index++;
            }
        }
    }

    @When("I find the latest created resource")
    public void iFindTheLatestCreatedResource() {
        response = resourceRequest.getResources();
        resourceList = resourceRequest.getResourcesEntity(response);
        lastResource = resourceList.get(resourceList.size() -1);
        resourceId = lastResource.getId();
        logger.info(lastResource);
    }

    @And("I update all parameters of this resource")
    public void  iUpdateAllParametersOfThisResource() {
        Resource newResource = DataGenerator.generateResource();
        response = resourceRequest.updateAllParameters(resourceId, newResource);
        logger.info(response.jsonPath().prettify());
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info(response.jsonPath().prettify());
    }



    @Then("the updated resource should be different from the old resource")
    public void theUpdatedResourceShouldBeDifferentFromTheOldResource() {
        resource = resourceRequest.getResourceEntity(response.jsonPath().prettify());

        Assert.assertNotEquals(resource.getName(), lastResource.getName());
        Assert.assertNotEquals(resource.getTrademark(), lastResource.getTrademark());
        Assert.assertNotEquals(resource.getStock(), lastResource.getStock());
        Assert.assertNotEquals(resource.getPrice(), lastResource.getPrice());
        Assert.assertNotEquals(resource.getDescription(), lastResource.getDescription());
        Assert.assertNotEquals(resource.getTags(), lastResource.getTags());

        logger.info("Finish");
    }
}
