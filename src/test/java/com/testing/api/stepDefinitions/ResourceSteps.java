package com.testing.api.stepDefinitions;
import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import com.testing.api.utils.DataGenerator;
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
    private Resource lastResource;
    private List<Resource> resourceActiveList;
    private List<Resource> resourceList;

    /**
     * Step to ensure there are at least a specified number of active resources in the system.
     * Creates default resources if needed.
     * @param numberResourcesNeeded The number of active resources required.
     */
    @Given("there are at least {int} active resources")
    public void thereAreAtLeastActiveResources(int numberResourcesNeeded) {
        response = resourceRequest.getActiveResources();
        if(response.statusCode() == 404){
            response = resourceRequest.createDefaultResource();
            logger.info("Resource created with response: {}", response.jsonPath().prettify());
        }
        response = resourceRequest.getActiveResources();
        Assert.assertEquals(200, response.statusCode());
        resourceActiveList = resourceRequest.getResourcesEntity(response);
        if(resourceActiveList.size() < numberResourcesNeeded){
            DataGenerator.generateResources(numberResourcesNeeded);
        }
        logger.info("Has at least the required number of active resources");
    }

    /**
     * Step to retrieve the list of all active resources.
     */
    @When("I get the list of all active resources")
    public void iGetTheListOfAllActiveResources() {
        response = resourceRequest.getActiveResources();
        resourceActiveList = resourceRequest.getResourcesEntity(response);
        logger.info("List of all active resources: {}", response.jsonPath().prettify());
    }

    /**
     * Step to validate that the response body matches the expected ResourceSchema schema.
     */
    @Then("the response body should match with the expected resource schema")
    public void theResponseBodyShouldMatchWithTheExpectedResourceSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource");
    }

    /**
     * Step to validate that the response body matches the expected ResourceListSchema schema.
     */
    @Then("the response body should match with the expected resource list schema")
    public void theResponseBodyShouldMatchWithTheExpectedResourceListSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource list");
    }

    /**
     * Step to update all active resources to inactive.
     * Iterates through the list of active resources and updates each one.
     */
    @Then("I update all active resources to inactive")
    public void iUpdateAllActiveResourcesToInactive() {
        int index = 0;
        while(index < resourceActiveList.size()){
            resource = resourceActiveList.get(index);
            resourceId = resource.getId();
            resource.setActive(false);
            response = resourceRequest.updateAllParameters(resourceId, resource);
            logger.info( "Resource updated {}", response.jsonPath().prettify());
            index++;
        }
    }

    /**
     * Step to verify that the response has the expected status code.
     * @param statusCode The expected status code of the response.
     */
    @Then("the response should have status code {int}")
    public void theResponseShouldHaveStatusCode(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info("The status code {}", response.statusCode());
    }

    /**
     * Step to ensure there are at least a specified number of resources in the system.
     * Creates default resources if needed.
     * @param numberResourcesNeeded The number of resources required in the system.
     */
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
            DataGenerator.generateResources(numberResourcesNeeded);
        }
        logger.info("Has at least the required number of resources");
    }

    /**
     * Step to find the most recently created resource and logs its details.
     */
    @When("I find the latest created resource")
    public void iFindTheLatestCreatedResource() {
        response = resourceRequest.getResources();
        resourceList = resourceRequest.getResourcesEntity(response);
        lastResource = resourceList.get(resourceList.size() -1);
        resourceId = lastResource.getId();
        logger.info("The latest created resource: {}", lastResource);
    }

    /**
     * Step to update all parameters of the specified resource.
     * Generates new resource data and sends an update request.
     */
    @When("I update all parameters of this resource")
    public void  iUpdateAllParametersOfThisResource() {
        Resource newResource = DataGenerator.generateResource();
        response = resourceRequest.updateAllParameters(resourceId, newResource);
        logger.info("Response to update all parameters {}", response.jsonPath().prettify());
    }

    /**
     * Step to ensure that the updated resource differs from the previous version.
     * Compares all fields of the updated resource with the previous resource.
     */
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
