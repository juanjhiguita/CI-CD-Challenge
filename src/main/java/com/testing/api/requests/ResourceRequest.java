package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceRequest extends BaseRequest{
    private String endpoint;
    /**
     * Get Resources list
     * @return rest-assured response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Parse response to get a list of Resource objects
     * @param response the API response
     * @return list of Resource objects
     */
    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    /**
     * Create resource
     * @param resource model
     * @return rest-assured response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Get the resource that have active field in true
     * @return rest-assured response
     */
    public Response getActiveResources() {
        try {
            boolean active = true;
            endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH , "?active=" + active);
            return requestGet(endpoint, createBaseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update active field of a resource by ID
     * @param resourceId the ID of the resource to update
     * @param resource the resource model with updated data
     * @return rest-assured response
     */
    public Response updateOnlyActive(String resourceId, Resource resource) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPatch(endpoint,createBaseHeaders(), resource);
    }

    /**
     * Update all fields of a resource by ID
     * @param resourceId the ID of the resource to update
     * @param resource the resource model with updated data
     * @return rest-assured response
     */
    public Response updateAllParameters(String resourceId, Resource resource) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Validate the response schema
     * @param response the API response
     * @param schemaPath path to the JSON schema file
     * @return true if schema validation passes, false otherwise
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Create a default resource from a JSON file
     * @return rest-assured response
     */
    public Response createDefaultResource() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createResource(jsonFile.getResourceByJson(Constants.DEFAULT_RESOURCE_FILE_PATH));
    }

    /**
     * Convert JSON string to Resource object
     * @param resourceJson JSON string representation of a resource
     * @return Resource object
     */
    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
    }
}
