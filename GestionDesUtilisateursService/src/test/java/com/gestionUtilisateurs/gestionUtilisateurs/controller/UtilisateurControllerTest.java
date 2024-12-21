package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.UtilisateurDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurControllerTest {

    @Mock
    private UserServiceItf userService;

    @InjectMocks
    private UtilisateurController utilisateurController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAuthenticatedUser() {
        Role mockRole = new Role();
        mockRole.setName(RoleEnum.USER);

        Utilisateur mockUser = new Utilisateur();
        mockUser.setRole(mockRole);
        mockUser.setEmail("test@example.com");
        mockUser.setPrenom("Test");
        mockUser.setNom("User");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        ResponseEntity<UtilisateurDTO> response = utilisateurController.authenticatedUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UtilisateurDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("USER", responseBody.getRole());
        assertEquals("test@example.com", responseBody.getUsername());
        assertEquals("test@example.com", responseBody.getEmail());
        assertEquals("Test", responseBody.getPrenom());
        assertEquals("User", responseBody.getNom());
    }


    @Test
    void testAllUsers() {
        List<Utilisateur> mockUsers = new ArrayList<>();
        when(userService.allUsers()).thenReturn(mockUsers);

        ResponseEntity<List<Utilisateur>> response = utilisateurController.allUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
    }

    @Test
    void testSetDateNaissance_validData() {
        String email = "user@example.com";
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);

        ResponseEntity<String> response = utilisateurController.setDateNaissance(email, dateNaissance);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Date de naissance mise à jour avec succès.", response.getBody());

        verify(userService, times(1)).updateDateNaissance(email, dateNaissance);
    }

    @Test
    void testSetDateNaissance_missingEmail() {
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);

        ResponseEntity<String> response = utilisateurController.setDateNaissance(null, dateNaissance);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email ou date de naissance manquants.", response.getBody());

        verify(userService, times(0)).updateDateNaissance(any(), any());
    }

    @Test
    void testSetDateNaissance_missingDateNaissance() {
        String email = "user@example.com";

        ResponseEntity<String> response = utilisateurController.setDateNaissance(email, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email ou date de naissance manquants.", response.getBody());

        verify(userService, times(0)).updateDateNaissance(any(), any());
    }

    @Test
    void testSetDateNaissance_serviceException() {
        String email = "user@example.com";
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);

        doThrow(new RuntimeException("Erreur interne")).when(userService).updateDateNaissance(email, dateNaissance);

        ResponseEntity<String> response = utilisateurController.setDateNaissance(email, dateNaissance);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Erreur lors de la mise à jour de la date de naissance"));

        verify(userService, times(1)).updateDateNaissance(email, dateNaissance);
    }
}