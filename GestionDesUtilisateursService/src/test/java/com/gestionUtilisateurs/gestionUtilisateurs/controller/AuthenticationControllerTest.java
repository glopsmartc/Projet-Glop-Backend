package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.*;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.AuthServiceItf;
import com.gestionUtilisateurs.gestionUtilisateurs.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


class AuthenticationControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthServiceItf authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setNom("testUser");
        registerUserDto.setPassword("password123");

        Utilisateur mockUser = new Utilisateur();
        mockUser.setNom("testUser");

        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(mockUser);

        ResponseEntity<Utilisateur> response = authenticationController.register(registerUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }
    @Test
    void testAuthenticate() {
        LoginUserDto loginUserDto = new LoginUserDto("testUser", "password123");
        Utilisateur mockUser = new Utilisateur();
        String mockToken = "mockToken";

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn(mockToken);

        ResponseEntity<LoginResponse> response = authenticationController.authenticate(loginUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockToken, response.getBody().getToken());
    }

    @Test
    void testForgotPassword() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");

        ResponseEntity<Map<String, String>> response = authenticationController.forgotPassword(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("L'email de réinitialisation a été envoyé avec succès.", response.getBody().get("message"));
    }




    @Test
    void testResetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("mockToken");
        request.setNewPassword("newPassword");

        ResponseEntity<Map<String, String>> response = authenticationController.resetPassword(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mot de passe réinitialisé avec succès.", response.getBody().get("message"));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setNom("testUser");
        registerUserDto.setPassword("password123");

        when(authenticationService.signup(any(RegisterUserDto.class)))
                .thenThrow(new RuntimeException("Utilisateur déjà existant"));

        assertThrows(RuntimeException.class, () -> authenticationController.register(registerUserDto));
    }
    @Test
    void testAuthenticate_InvalidCredentials() {
        LoginUserDto loginUserDto = new LoginUserDto("wrongUser", "wrongPassword");

        when(authenticationService.authenticate(any(LoginUserDto.class)))
                .thenThrow(new RuntimeException("Identifiants invalides"));

        assertThrows(RuntimeException.class, () -> authenticationController.authenticate(loginUserDto));
    }

    @Test
    void testForgotPassword_InvalidEmail() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("invalid@example.com");

        doThrow(new IllegalArgumentException("Email non trouvé"))
                .when(authenticationService).initiatePasswordReset("invalid@example.com");

        ResponseEntity<Map<String, String>> response = authenticationController.forgotPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email non valide : Email non trouvé", response.getBody().get("error"));
    }

    @Test
    void testForgotPassword_InternalError() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");

        doThrow(new RuntimeException("Erreur serveur"))
                .when(authenticationService).initiatePasswordReset("test@example.com");

        ResponseEntity<Map<String, String>> response = authenticationController.forgotPassword(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Une erreur inattendue s'est produite.", response.getBody().get("error"));
    }
}