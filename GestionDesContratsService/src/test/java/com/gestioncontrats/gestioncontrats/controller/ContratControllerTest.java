package com.gestioncontrats.gestioncontrats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.dto.UtilisateurDTO;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ContratControllerTest {

    @Mock
    private ContratServiceItf contratService;

    @Mock
    private UserClientService utilisateurService;

    @InjectMocks
    private ContratController contratController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateContract_withMatchingOffre() throws IOException {
        Contrat contrat = new Contrat();
        Offre offre = new Offre();
        contrat.setOffre(offre);

        MultipartFile pdfFile = Mockito.mock(MultipartFile.class);
        when(pdfFile.getContentType()).thenReturn("application/pdf");

        when(contratService.createContract(any(CreateContractRequest.class), eq(pdfFile), anyString())).thenReturn(contrat);

        String authorizationHeader = "Bearer fake_token";

        ResponseEntity<?> response = contratController.createContract(authorizationHeader, "{}", pdfFile);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contrat, response.getBody());
    }


    @Test
    void testCreateContract_noMatchingOffre() throws IOException {
        Contrat contrat = new Contrat();
        contrat.setOffre(null);

        when(contratService.createContract(any(CreateContractRequest.class), any(MultipartFile.class), any(String.class)))
                .thenReturn(contrat);

        CreateContractRequest request = new CreateContractRequest();
        String requestJson = new ObjectMapper().writeValueAsString(request);

        MultipartFile pdfFile = new MockMultipartFile("file", "dummy.pdf", "application/pdf", new byte[0]);

        String authorizationHeader = "Bearer dummy-token";

        ResponseEntity<?> response = contratController.createContract(authorizationHeader, requestJson, pdfFile);

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

    @Test
    void testAllOffres() {
        List<Offre> offres = new ArrayList<>();
        offres.add(new Offre());

        when(contratService.getAllOffres()).thenReturn(offres);

        ResponseEntity<List<Offre>> response = contratController.allOffres();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAllContrats() {
        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat());

        when(contratService.getAllContrats()).thenReturn(contrats);

        ResponseEntity<List<Contrat>> response = contratController.allContrats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetContratById_found() {
        Contrat contrat = new Contrat();
        contrat.setId(1L);

        when(contratService.getContratById(1L)).thenReturn(java.util.Optional.of(contrat));

        ResponseEntity<Contrat> response = contratController.getContratById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contrat, response.getBody());
    }

    @Test
    void testGetContratById_notFound() {
        when(contratService.getContratById(1L)).thenReturn(java.util.Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            contratController.getContratById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Contrat non trouvé.", exception.getReason());
    }

    @Test
    void testGetCurrentContract_notAuthenticated() {
        SecurityContextHolder.clearContext();

        ResponseEntity<String> response = contratController.getCurrentContract();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Accès refusé.", response.getBody());
    }

    @Test
    void testGetUserContracts_found() {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("client@example.com");

        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat());

        when(utilisateurService.getAuthenticatedUser(anyString())).thenReturn(utilisateurDTO);
        when(contratService.getContratsByClientEmail("client@example.com")).thenReturn(contrats);

        ResponseEntity<List<Contrat>> response = contratController.getUserContracts("Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetUserContracts_notFound() {
        when(utilisateurService.getAuthenticatedUser(anyString())).thenReturn(new UtilisateurDTO());
        when(contratService.getContratsByClientEmail(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Contrat>> response = contratController.getUserContracts("Bearer token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetCurrentUser() {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("client@example.com");

        when(utilisateurService.getAuthenticatedUser(anyString())).thenReturn(utilisateurDTO);

        String authorizationHeader = "Bearer dummy-token";

        UtilisateurDTO response = contratController.getCurrentUser(authorizationHeader);

        assertEquals("client@example.com", response.getEmail());
    }

    @Test
    void testGetContratById_invalidId() {
        when(contratService.getContratById(anyLong())).thenReturn(java.util.Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            contratController.getContratById(999L); // Invalid ID
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Contrat non trouvé.", exception.getReason());
    }

    @Test
    void testAllOffres_noOffersFound() {
        List<Offre> offres = new ArrayList<>();
        when(contratService.getAllOffres()).thenReturn(offres);

        ResponseEntity<List<Offre>> response = contratController.allOffres();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetOffreCorrespondante_withNoMatchingOffre() {
        when(contratService.findMatchingOffre(any(CreateContractRequest.class))).thenReturn(null);

        CreateContractRequest request = new CreateContractRequest();
        ResponseEntity<?> response = contratController.getOffreCorrespondante(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Aucune offre correspondante trouvée.", response.getBody());
    }

    @Test
    void testAllContrats_noContratsFound() {
        List<Contrat> contrats = new ArrayList<>();
        when(contratService.getAllContrats()).thenReturn(contrats);

        ResponseEntity<List<Contrat>> response = contratController.allContrats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }


}
