package com.aiapitest.tests;

import com.aiapitest.client.SampleApiClient;
import com.aiapitest.validator.GPTValidator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SampleApiTest {

    @Test
    public void validateJokeResponse() {
        SampleApiClient client = new SampleApiClient("https://api.chucknorris.io");
        String joke = client.getJoke();

        GPTValidator validator = new GPTValidator();
        boolean isValid = validator.validate("Tell me a joke", joke);

        assertTrue(isValid, "Joke response was not validated by OpenAI");
    }
}
