package com.gestioncontrats.gestioncontrats.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testForbiddenAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some-secured-endpoint"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testCorsConfiguration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/some-secured-endpoint")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }


    @Test
    void testCorsUnauthorizedOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/some-secured-endpoint")
                        .header("Origin", "http://unauthorized-origin.com"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
