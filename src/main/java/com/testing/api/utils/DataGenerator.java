package com.testing.api.utils;

import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import net.datafaker.Faker;

public class DataGenerator {

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
}
