package com.testing.api.stepDefinitions;

import com.testing.api.requests.ClientRequest;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

public class BaseSteps {

    protected static final Logger logger = LogManager.getLogger(ClientSteps.class);
    protected Response response;

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info(response.jsonPath().prettify());
    }
}
