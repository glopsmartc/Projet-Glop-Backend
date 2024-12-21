package com.gestioncontrats.gestioncontrats.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class RestConfigurationTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateBeanShouldBeLoaded() {
        assertNotNull(restTemplate, "Le bean RestTemplate n'a pas été chargé.");
    }
}
