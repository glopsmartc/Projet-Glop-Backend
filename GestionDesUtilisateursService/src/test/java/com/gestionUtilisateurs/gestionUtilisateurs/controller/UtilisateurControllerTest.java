package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.controller.UtilisateurController;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        // Mock the security context and authentication
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
}