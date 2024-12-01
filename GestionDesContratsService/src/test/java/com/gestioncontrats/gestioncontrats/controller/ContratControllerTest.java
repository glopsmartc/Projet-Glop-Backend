package com.gestioncontrats.gestioncontrats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    void testCreateContract_withMatchingOffre() throws IOException, IOException {
        // Mock des objets nécessaires
        Contrat contrat = new Contrat();
        Offre offre = new Offre();
        contrat.setOffre(offre);

        // Création d'un fichier PDF simulé
        MultipartFile pdfFile = Mockito.mock(MultipartFile.class);
        when(pdfFile.getContentType()).thenReturn("application/pdf");

        // Simulation du service
        when(contratService.createContract(any(CreateContractRequest.class), eq(pdfFile), anyString())).thenReturn(contrat);

        CreateContractRequest request = new CreateContractRequest();
        String authorizationHeader = "Bearer fake_token";

        // Appel du contrôleur
        ResponseEntity<?> response = contratController.createContract(authorizationHeader, "{}", pdfFile);

        // Vérifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contrat, response.getBody());
    }


    @Test
    void testCreateContract_noMatchingOffre() throws IOException {
        // Créer un contrat sans offre
        Contrat contrat = new Contrat();
        contrat.setOffre(null);

        // Simulation du comportement du service
        when(contratService.createContract(any(CreateContractRequest.class), any(MultipartFile.class), any(String.class)))
                .thenReturn(contrat);

        // Créer un objet CreateContractRequest
        CreateContractRequest request = new CreateContractRequest();
        String requestJson = new ObjectMapper().writeValueAsString(request); // Convertir en JSON

        // Créer un fichier PDF simulé
        MultipartFile pdfFile = new MockMultipartFile("file", "dummy.pdf", "application/pdf", new byte[0]);

        // Simuler l'en-tête Authorization
        String authorizationHeader = "Bearer dummy-token";

        // Appeler la méthode createContract avec les bons paramètres
        ResponseEntity<?> response = contratController.createContract(authorizationHeader, requestJson, pdfFile);

        // Vérifier que la réponse est une mauvaise demande (BAD_REQUEST)
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
