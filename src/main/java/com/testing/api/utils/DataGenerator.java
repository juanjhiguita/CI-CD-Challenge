package com.testing.api.utils;

import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.requests.ClientRequest;
import com.testing.api.requests.ResourceRequest;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataGenerator {
    private static final Logger logger = LogManager.getLogger(DataGenerator.class);

    public static Resource generateResource(){
        Faker faker = new Faker();
        String name = faker.commerce().productName();
        String trademark = faker.company().name();
        int stock = faker.number().positive();
        float price = faker.number().randomNumber(6, true);
        String description = faker.lorem().sentence();
        String tags = faker.lorem().word();
        boolean active = true;

        Resource newResource = new Resource();
        newResource.setName(name);
        newResource.setTrademark(trademark);
        newResource.setStock(stock);
        newResource.setPrice(price);
        newResource.setDescription(description);
        newResource.setTags(tags);
        newResource.setActive(active);
        return newResource;
    }

    public static Client generateClient(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String country = faker.country().name();
        String city = faker.address().cityName();
        String email = faker.internet().emailAddress();
        String phone = faker.phoneNumber().phoneNumber();

        Client newClient = new Client();
        newClient.setName(name);
        newClient.setLastName(lastName);
        newClient.setCountry(country);
        newClient.setCity(city);
        newClient.setEmail(email);
        newClient.setPhone(phone);
        return newClient;
    }

    public static String generatePhoneNumber(){
        Faker faker = new Faker();
        return faker.phoneNumber().phoneNumber();
    }

    public static void generateClients(int numberClientsToGenerate){
        int index = 0;
        while(index < numberClientsToGenerate){
            Client newClient = DataGenerator.generateClient();
            ClientRequest clientRequest = new ClientRequest();
            Response creationResponse = clientRequest.createClient(newClient);
            logger.info("Client created with response: {}", creationResponse.jsonPath().prettify());
            index++;
        }
    }

    public static void generateResources(int numberResourcesToGenerate){
        int index = 0;
        while(index < numberResourcesToGenerate){
            Resource newResource = DataGenerator.generateResource();
            ResourceRequest resourceRequest = new ResourceRequest();
            Response creationResponse = resourceRequest.createResource(newResource);
            logger.info("Resource created with response: {}", creationResponse.jsonPath().prettify());
            index++;
        }
    }
}
