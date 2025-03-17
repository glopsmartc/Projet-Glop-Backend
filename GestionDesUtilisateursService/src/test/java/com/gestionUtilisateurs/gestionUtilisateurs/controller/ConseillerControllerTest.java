package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ConseillerControllerTest {

    @Mock
    private UserServiceItf userService;

    @InjectMocks
    private ConseillerController conseillerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateConseiller() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        Utilisateur mockUser = new Utilisateur();

        when(userService.createConseiller(any(RegisterUserDto.class))).thenReturn(mockUser);

        ResponseEntity<Utilisateur> response = conseillerController.createConseiller(registerUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }


}
