package com.gestioncontrats.gestioncontrats.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class EmissionController {

    private final String API_URL = "https://impactco2.fr/api/v1/transport";
    private final String API_KEY = "4fd577a1-4bac-49aa-acdd-fb2ef9d7d294";

    @GetMapping("/emissions")
    public ResponseEntity<?> getEmissions(@RequestParam("km") int km, @RequestParam("transports") int transportId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = API_URL + "?km=" + km + "&transports=" + transportId;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error communicating with Impact CO2 API");
        }
    }
}
