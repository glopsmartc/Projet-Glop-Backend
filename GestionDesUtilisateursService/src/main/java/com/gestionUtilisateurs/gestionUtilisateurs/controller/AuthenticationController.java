package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginResponse;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.exception.AddFailedException;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.AuthServiceItf;
import com.gestionUtilisateurs.gestionUtilisateurs.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthServiceItf authenticationService;

    public AuthenticationController(JwtService jwtService, AuthServiceItf authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

        @PostMapping("/signup")
        public ResponseEntity<Utilisateur> register(@RequestBody RegisterUserDto registerUserDto) {
            Utilisateur registeredUser = authenticationService.signup(registerUserDto);
            System.out.println("hi"+registeredUser);
            return ResponseEntity.ok(registeredUser);
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
            Utilisateur authenticatedUser = authenticationService.authenticate(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        }
}
