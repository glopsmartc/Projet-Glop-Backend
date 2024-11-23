package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.dto.AccompagnantDto;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.ContratRepository;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ContratServiceImpTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private OffreRepository offreRepository;

    @InjectMocks
    private ContratServiceImp contratServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateContract_withMatchingOffre() {
        CreateContractRequest request = new CreateContractRequest();
        request.setDureeContrat("12");
        request.setAssurerTransport(true);
        request.setAssurerPersonnes(true);

        request.setAccompagnants(new ArrayList<>());
        AccompagnantDto accompagnant = new AccompagnantDto();
        accompagnant.setNom("Doe");
        accompagnant.setPrenom("John");
        request.getAccompagnants().add(accompagnant);
    }


    @Test
    void testCreateContract_noMatchingOffre() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);
        request.setAssurerPersonnes(true);
        request.setAccompagnants(new ArrayList<>());

        when(offreRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.createContract(request)
        );

        assertEquals("Aucune offre correspondante trouvée.", exception.getMessage());
    }


    @Test
    void testFindMatchingOffre_withMatchingOffre() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);

        Offre matchingOffre = new Offre();
        matchingOffre.setNomOffre("AvecMoyTra");
        when(offreRepository.findAll()).thenReturn(List.of(matchingOffre));

        Offre result = contratServiceImp.findMatchingOffre(request);

        assertEquals(matchingOffre, result);
    }

    @Test
    void testFindMatchingOffre_noMatchingOffre() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);

        Offre nonMatchingOffre = new Offre();
        nonMatchingOffre.setNomOffre("SansMoyTra");
        when(offreRepository.findAll()).thenReturn(List.of(nonMatchingOffre));

        Offre result = contratServiceImp.findMatchingOffre(request);

        assertEquals(null, result);
    }
}
