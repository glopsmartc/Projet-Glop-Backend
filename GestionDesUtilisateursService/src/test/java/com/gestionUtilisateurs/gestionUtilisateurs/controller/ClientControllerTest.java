package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.MaladieChroniqueDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.service.ClientServiceItf;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private UserServiceItf userService;

    @Mock
    private ClientServiceItf clientServiceItf;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testSetMaladies_Failure() {
        MaladieChroniqueDTO maladieChroniqueDTO = new MaladieChroniqueDTO(true, "Diabète de type 2", "user@example.com");

        String errorMessage = "Erreur base de données";
        doThrow(new RuntimeException(errorMessage))
                .when(userService).updateMaladieChronique(any(MaladieChroniqueDTO.class));

        ResponseEntity<String> response = clientController.setMaladies(maladieChroniqueDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Erreur lors de la mise à jour des maladies chroniques"));
        assertTrue(response.getBody().contains(errorMessage));

        verify(userService, times(1)).updateMaladieChronique(maladieChroniqueDTO);
    }

    @Test
    void testAllClients_Success() {
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(new Client());

        when(clientServiceItf.allClients()).thenReturn(mockClients);

        ResponseEntity<List<Client>> response = clientController.allClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClients, response.getBody());
    }

    @Test
    void testAllClients_EmptyList() {
        when(clientServiceItf.allClients()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Client>> response = clientController.allClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}