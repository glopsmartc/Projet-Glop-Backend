package com.gestioncontrats.gestioncontrats.config;

import com.gestioncontrats.gestioncontrats.dto.UtilisateurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClientService userClientService;

    private final String utilisateurServiceUrl = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userClientService = new UserClientService(restTemplate);
        userClientService.utilisateurServiceUrl = utilisateurServiceUrl;
    }

    @Test
    void testGetAuthenticatedUser() {
        String token = "test-token";
        String url = utilisateurServiceUrl + "/users/current-user";
        UtilisateurDTO mockUser = new UtilisateurDTO();
        mockUser.setEmail("test@example.com");
        ResponseEntity<UtilisateurDTO> responseEntity = new ResponseEntity<>(mockUser, HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(UtilisateurDTO.class)))
                .thenReturn(responseEntity);


        UtilisateurDTO result = userClientService.getAuthenticatedUser(token);


        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());


        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), any(), eq(UtilisateurDTO.class));
    }

    @Test
    void testSetDateNaissance() {

        String token = "test-token";
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);
        String email = "test@example.com";
        String url = utilisateurServiceUrl + "/users/setDateNaissance?dateNaissance="
                + dateNaissance + "&email=" + email;

        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.POST), any(), eq(Void.class)))
                .thenReturn(responseEntity);

        userClientService.setDateNaissance(token, dateNaissance, email);

        verify(restTemplate).exchange(eq(url), eq(HttpMethod.POST), any(), eq(Void.class));
    }

}
