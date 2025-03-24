package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImpTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImp clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllClients_Success() {
        List<Client> mockClients = new ArrayList<>();
        Client client1 = new Client();
        Client client2 = new Client();
        mockClients.add(client1);
        mockClients.add(client2);

        when(clientRepository.findAll()).thenReturn(mockClients);

        List<Client> result = clientService.allClients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockClients, result);

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testAllClients_EmptyList() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());

        List<Client> result = clientService.allClients();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testAllClients_RepositoryException() {
        when(clientRepository.findAll()).thenThrow(new RuntimeException("Erreur base de données"));
        assertThrows(RuntimeException.class, () -> clientService.allClients());
        verify(clientRepository, times(1)).findAll();
    }
}