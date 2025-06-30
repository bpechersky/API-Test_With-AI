package com.aiapitest.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SampleApiClient {
    private final String baseUrl;

    public SampleApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getJoke() {
        Response response = RestAssured
            .given()
            .get(baseUrl + "/jokes/random");

        return response.jsonPath().getString("value");
    }
}
