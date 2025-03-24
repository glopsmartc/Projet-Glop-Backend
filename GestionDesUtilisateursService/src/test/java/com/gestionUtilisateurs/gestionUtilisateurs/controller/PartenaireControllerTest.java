package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;
import com.gestionUtilisateurs.gestionUtilisateurs.service.PartenaireServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PartenaireControllerTest {

    @Mock
    private PartenaireServiceItf partenaireServiceItf;

    @InjectMocks
    private PartenaireController partenaireController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllPartenaires_Success() {
        List<Partenaire> mockPartenaires = new ArrayList<>();
        mockPartenaires.add(new Partenaire());
        mockPartenaires.add(new Partenaire());

        when(partenaireServiceItf.allPartenaires()).thenReturn(mockPartenaires);

        ResponseEntity<List<Partenaire>> response = partenaireController.allPartenaires();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPartenaires, response.getBody());
        assertEquals(2, response.getBody().size());

        verify(partenaireServiceItf, times(1)).allPartenaires();
    }

    @Test
    void testAllPartenaires_EmptyList() {
        when(partenaireServiceItf.allPartenaires()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Partenaire>> response = partenaireController.allPartenaires();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        verify(partenaireServiceItf, times(1)).allPartenaires();
    }

    @Test
    void testGetPartenaireById_Found() {
        Long id = 1L;
        Partenaire mockPartenaire = new Partenaire();

        when(partenaireServiceItf.getPartenaireById(id)).thenReturn(Optional.of(mockPartenaire));

        ResponseEntity<Partenaire> response = partenaireController.getPartenaireById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPartenaire, response.getBody());

        verify(partenaireServiceItf, times(1)).getPartenaireById(id);
    }

    @Test
    void testGetPartenaireById_NotFound() {
        Long id = 999L;

        when(partenaireServiceItf.getPartenaireById(id)).thenReturn(Optional.empty());

        ResponseEntity<Partenaire> response = partenaireController.getPartenaireById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());


        verify(partenaireServiceItf, times(1)).getPartenaireById(id);
    }
}