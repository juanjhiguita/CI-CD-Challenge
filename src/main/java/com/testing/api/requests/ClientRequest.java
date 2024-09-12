package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ClientRequest extends BaseRequest{
    private String endpoint;
    /**
     * Get Client list
     * @return rest-assured response
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get clients by name
     * @param name the name to filter clients by
     * @return rest-assured response
     */
    public Response getClientsByName(String name) {
        try {
            endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH , "?name=" + name);
            return requestGet(endpoint, createBaseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get client by ID
     * @param id the ID of the client to retrieve
     * @return rest-assured response
     */
    public Response getClientById(String id) {
        try {
            endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH , "?id=" + id);
            return requestGet(endpoint, createBaseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse response to get a list of Client objects
     * @param response the API response
     * @return list of Client objects
     */
    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    /**
     * Create client
     * @param client model
     * @return rest-assured response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    /**
     * Create a default client from a JSON file
     * @return rest-assured response
     */
    public Response createDefaultClient() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createClient(jsonFile.getClientByJson(Constants.DEFAULT_CLIENT_FILE_PATH));
    }

    /**
     * Update the phone number of a client
     * @param clientId the ID of the client to update
     * @param client the client model with updated data
     * @return rest-assured response
     */
    public Response updatePhoneNumber(String clientId, Client client) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     * Update only the phone number of a client
     * @param clientId the ID of the client to update
     * @param requestBody the JSON body containing the new phone number
     * @return rest-assured response
     */
    public Response updateOnlyPhoneNumber(String clientId, String requestBody) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPatch(endpoint,createBaseHeaders(), requestBody);
    }

    /**
     * Convert JSON string to Client object
     * @param clientJson JSON string representation of a client
     * @return Client object
     */
    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
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
            return true; // Return true if the assertion passes
        } catch (AssertionError e) {
            // Assertion failed, return false
            return false;
        }
    }

    /**
     * Check if the response body includes phone number
     * @param response the API response
     * @return true if phone number is included, false otherwise
     */
    public boolean includePhoneNumber(Response response) {
        try{
            String responseBody = response.getBody().asString();
            if (responseBody.contains("phone")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a client by ID
     * @param clientId the ID of the client to delete
     * @return rest-assured response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * Validate if the update response body is correct
     * @param response the API response
     * @return true if the response contains all expected fields, false otherwise
     */
    public boolean isUpdateResponseBodyValid(Response response) {
        try{
            String responseBody = response.getBody().asString();
            if (responseBody.contains("name") && responseBody.contains("lastName") && responseBody.contains("country") &&
                    responseBody.contains("city") && responseBody.contains("email") && responseBody.contains("phone")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
