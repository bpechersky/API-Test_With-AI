package com.aiapitest.validator;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GPTValidator {
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";
    private final String apiKey;

    public GPTValidator() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OPENAI_API_KEY not set");
        }
    }

    public boolean validate(String question, String actualAnswer) {
        String prompt = "User asked: \"" + question + "\" and the bot replied: \"" + actualAnswer + "\". Is this an appropriate and useful response? Answer yes or no.";

        Response response = RestAssured.given()
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .body("{\"model\": \"text-davinci-003\", \"prompt\": \"" + prompt + "\", \"max_tokens\": 5}")
            .post(OPENAI_URL);

        String result = response.jsonPath().getString("choices[0].text").trim().toLowerCase();
        return result.contains("yes");
    }
}
