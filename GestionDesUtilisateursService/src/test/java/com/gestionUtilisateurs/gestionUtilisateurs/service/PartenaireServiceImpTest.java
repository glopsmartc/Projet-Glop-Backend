package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;
import com.gestionUtilisateurs.gestionUtilisateurs.model.PartenaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartenaireServiceImpTest {

    @Mock
    private PartenaireRepository partenaireRepository;

    @InjectMocks
    private PartenaireServiceImp partenaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllPartenaires_Success() {
        List<Partenaire> mockPartenaires = new ArrayList<>();
        Partenaire partenaire1 = new Partenaire();
        Partenaire partenaire2 = new Partenaire();
        mockPartenaires.add(partenaire1);
        mockPartenaires.add(partenaire2);

        when(partenaireRepository.findAll()).thenReturn(mockPartenaires);

        List<Partenaire> result = partenaireService.allPartenaires();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPartenaires, result);

        verify(partenaireRepository, times(1)).findAll();
    }

    @Test
    void testAllPartenaires_EmptyList() {
        when(partenaireRepository.findAll()).thenReturn(new ArrayList<>());

        List<Partenaire> result = partenaireService.allPartenaires();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(partenaireRepository, times(1)).findAll();
    }

    @Test
    void testGetPartenaireById_Found() {
        Long id = 1L;
        Partenaire mockPartenaire = new Partenaire();

        when(partenaireRepository.findById(id)).thenReturn(Optional.of(mockPartenaire));

        Optional<Partenaire> result = partenaireService.getPartenaireById(id);

        assertTrue(result.isPresent());
        assertEquals(mockPartenaire, result.get());

        verify(partenaireRepository, times(1)).findById(id);
    }

    @Test
    void testGetPartenaireById_NotFound() {
        Long id = 999L;

        when(partenaireRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Partenaire> result = partenaireService.getPartenaireById(id);

        assertFalse(result.isPresent());

        verify(partenaireRepository, times(1)).findById(id);
    }
}