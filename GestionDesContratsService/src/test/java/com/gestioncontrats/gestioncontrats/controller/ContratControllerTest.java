package com.gestioncontrats.gestioncontrats.controller;

import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import com.gestioncontrats.gestioncontrats.service.ContratServiceItf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContratControllerTest {

    @Mock
    private ContratServiceItf contratService;

    @Mock
    private OffreRepository offreRepository;

    @Mock
    private UserClientService utilisateurService;

    @InjectMocks
    private ContratController contratController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateContract_withMatchingOffre() {
        Contrat contrat = new Contrat();
        Offre offre = new Offre();
        contrat.setOffre(offre);

        when(contratService.createContract(any(CreateContractRequest.class))).thenReturn(contrat);

        CreateContractRequest request = new CreateContractRequest();

        ResponseEntity<?> response = contratController.createContract(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contrat, response.getBody());
    }

    @Test
    void testCreateContract_noMatchingOffre() {
        Contrat contrat = new Contrat();
        contrat.setOffre(null);

        when(contratService.createContract(any(CreateContractRequest.class))).thenReturn(contrat);

        CreateContractRequest request = new CreateContractRequest();

        ResponseEntity<?> response = contratController.createContract(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Aucune offre correspondante trouvée.", response.getBody());
    }

    @Test
    void testGetOffreCorrespondante_withMatchingOffre() {
        Offre offre = new Offre();
        OffreResponse expectedResponse = new OffreResponse();
        when(contratService.findMatchingOffre(any(CreateContractRequest.class))).thenReturn(offre);
        when(contratService.buildOffreResponse(any(Offre.class), any(CreateContractRequest.class))).thenReturn(expectedResponse);

        CreateContractRequest request = new CreateContractRequest();

        ResponseEntity<?> response = contratController.getOffreCorrespondante(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetOffreCorrespondante_noMatchingOffre() {
        when(contratService.findMatchingOffre(any(CreateContractRequest.class))).thenReturn(null);

        CreateContractRequest request = new CreateContractRequest();

        ResponseEntity<?> response = contratController.getOffreCorrespondante(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Aucune offre correspondante trouvée.", response.getBody());
    }
}
