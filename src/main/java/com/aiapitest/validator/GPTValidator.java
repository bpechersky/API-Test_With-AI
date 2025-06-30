package com.aiapitest.validator;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GPTValidator {
    // OpenAI API URL for completions
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";
    private final String apiKey;

    // Constructor to initialize the API key
    public GPTValidator() {
        this.apiKey = System.getenv("OPENAI_API_KEY");

        // Ensure API key is set in the environment variables
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OPENAI_API_KEY not set");
        }
    }

    // Validate the response from OpenAI
    public boolean validate(String question, String actualAnswer) {
        // Prepare the prompt
        String prompt = "User asked: \"" + question + "\" and the bot replied: \"" + actualAnswer + "\". Is this an appropriate and useful response? Answer yes or no.";

        // Use Gson to create the request body to ensure proper JSON formatting
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");  // Updated model name
        requestBody.addProperty("prompt", prompt);
        requestBody.addProperty("max_tokens", 5);

        // Send the request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())  // Convert the JSON object to a string
                .post(OPENAI_URL);

        // Check for a valid response status
        if (response.statusCode() != 200) {
            System.out.println("Error: API returned status code " + response.statusCode());
            System.out.println("Response body: " + response.asString());  // Print the response for debugging
            return false;
        }

        // Print out the response for debugging
        System.out.println("OpenAI Response: " + response.asString());

        // Process the response
        String result = response.jsonPath().getString("choices[0].text");
        if (result == null) {
            System.out.println("Error: No valid response from OpenAI.");
            return false;
        }

        return result.trim().toLowerCase().contains("yes");
    }
}
