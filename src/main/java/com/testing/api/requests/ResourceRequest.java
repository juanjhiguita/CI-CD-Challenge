package com.testing.api.requests;

import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
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

    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

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

    public Response getInactiveResources() {
        try {
            boolean active = false;
            endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH , "?active=" + active);
            return requestGet(endpoint, createBaseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response updateActive(String resourceId, Resource resource) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint,createBaseHeaders(), resource);
    }

    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true; // Return true if the assertion passes
        } catch (AssertionError e) {
            // Assertion failed, return false
            return false;
        }
    }


}
