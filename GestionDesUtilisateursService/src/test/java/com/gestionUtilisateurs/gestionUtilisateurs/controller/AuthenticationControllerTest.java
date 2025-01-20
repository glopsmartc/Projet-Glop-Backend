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

import static org.mockito.ArgumentMatchers.any;
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

}