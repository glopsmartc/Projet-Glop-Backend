package com.gestioncontrats.gestioncontrats.service;
import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.AccompagnantDto;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.dto.UtilisateurDTO;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.ContratRepository;
import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class ContratServiceImpTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private UserClientService userClientService;

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

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("test@example.com");
        when(userClientService.getAuthenticatedUser(anyString())).thenReturn(utilisateurDTO);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.createContract(request, null, "valid-token")
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

        assertNull(result);
    }

    @Test
    void testCreateContract_invalidOffre() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);
        request.setAssurerPersonnes(true);
        request.setAccompagnants(new ArrayList<>());

        when(offreRepository.findByNomOffre(anyString())).thenReturn(null);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("test@example.com");
        when(userClientService.getAuthenticatedUser(anyString())).thenReturn(utilisateurDTO);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.createContract(request, null, "valid-token")
        );

        assertEquals("Aucune offre correspondante trouvée.", exception.getMessage());
    }



    @Test
    void testFindMatchingOffre_noOfferMatches() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);

        Offre nonMatchingOffre = new Offre();
        nonMatchingOffre.setNomOffre("OffreSansMoyTra");

        when(offreRepository.findAll()).thenReturn(List.of(nonMatchingOffre));

        Offre result = contratServiceImp.findMatchingOffre(request);

        assertNull(result);
    }
    @Test
    void testGetContratById_found() {
        Contrat contrat = new Contrat();
        contrat.setId(1L);
        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));

        Optional<Contrat> result = contratServiceImp.getContratById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }


    @Test
    void testFindMatchingOffre_withAssuranceTransport() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);

        Offre matchingOffre = new Offre();
        matchingOffre.setNomOffre("AvecMoyTra");
        when(offreRepository.findAll()).thenReturn(List.of(matchingOffre));

        Offre result = contratServiceImp.findMatchingOffre(request);

        assertNotNull(result);
        assertEquals(matchingOffre, result);
    }



    @Test
    void testParsePrix() throws Exception {
        CreateContractRequest request = new CreateContractRequest();
        request.setNombrePersonnes(2);

        Offre offre = new Offre();
        offre.setPrixMin("50€/personneAccompagnante");

        Method parsePrixMethod = ContratServiceImp.class.getDeclaredMethod("parsePrix", String.class, int.class);
        parsePrixMethod.setAccessible(true);

        double prix = (double) parsePrixMethod.invoke(contratServiceImp, offre.getPrixMin(), request.getNombrePersonnes());

        assertEquals(100.0, prix);
    }


    @Test
    void testSavePdfFile() throws IOException, NoSuchFieldException, IllegalAccessException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");

        String safeStoragePath = System.getProperty("user.home") + "/Documents/storage";

        Field storagePathField = ContratServiceImp.class.getDeclaredField("storagePath");
        storagePathField.setAccessible(true);

        storagePathField.set(contratServiceImp, safeStoragePath);

        String result = contratServiceImp.savePdfFile(file, 1L);

        assertTrue(result.contains("1_test.pdf"));
    }


    @Test
    void testGetContratById_noFound() {
        when(contratRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Contrat> result = contratServiceImp.getContratById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testBuildOffreResponse_withDifferentDurations() {
        Locale.setDefault(Locale.US);

        CreateContractRequest request = new CreateContractRequest();
        request.setDureeContrat("3_mois");

        Offre offre = new Offre();
        offre.setPrixMin("100€/personneAccompagnante");
        offre.setPrixMax("150€/personneAccompagnante");

        OffreResponse response = contratServiceImp.buildOffreResponse(offre, request);

        assertEquals("300.00€", response.getPrixMinTotal());
        assertEquals("450.00€", response.getPrixMaxTotal());
    }

    @Test
    void testParsePrix_withPersonneAccompagnante() throws Exception {
        CreateContractRequest request = new CreateContractRequest();
        request.setNombrePersonnes(3);

        Offre offre = new Offre();
        offre.setPrixMin("50€/personneAccompagnante");

        Method method = ContratServiceImp.class.getDeclaredMethod("parsePrix", String.class, int.class);
        method.setAccessible(true);

        double prix = (double) method.invoke(contratServiceImp, offre.getPrixMin(), request.getNombrePersonnes());
        assertEquals(150.0, prix);
    }

    @Test
    void testParsePrix_withoutPersonneAccompagnante() throws Exception {
        CreateContractRequest request = new CreateContractRequest();
        request.setNombrePersonnes(1);

        Offre offre = new Offre();
        offre.setPrixMin("100€");

        Method method = ContratServiceImp.class.getDeclaredMethod("parsePrix", String.class, int.class);
        method.setAccessible(true);

        double prix = (double) method.invoke(contratServiceImp, offre.getPrixMin(), request.getNombrePersonnes());
        assertEquals(100.0, prix);
    }
    @Test
    void testBuildOffreResponse_invalidDuration() {
        CreateContractRequest request = new CreateContractRequest();
        request.setDureeContrat("invalid_duration");

        Offre offre = new Offre();
        offre.setPrixMin("100€/personneAccompagnante");
        offre.setPrixMax("150€/personneAccompagnante");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.buildOffreResponse(offre, request)
        );

        assertEquals("Durée du contrat invalide", exception.getMessage());
    }

    @Test
    void testGetAllOffres() {
        // Création d'une liste d'offres simulées
        Offre offre1 = new Offre();
        offre1.setNomOffre("Offre1");
        Offre offre2 = new Offre();
        offre2.setNomOffre("Offre2");

        when(offreRepository.findAll()).thenReturn(List.of(offre1, offre2));

        List<Offre> result = contratServiceImp.getAllOffres();

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Offre1", result.get(0).getNomOffre());
        assertEquals("Offre2", result.get(1).getNomOffre());
    }

    @Test
    void testGetAllContrats() {
        // Création d'une liste de contrats simulés
        Contrat contrat1 = new Contrat();
        contrat1.setId(1L);
        Contrat contrat2 = new Contrat();
        contrat2.setId(2L);

        when(contratRepository.findAll()).thenReturn(List.of(contrat1, contrat2));

        List<Contrat> result = contratServiceImp.getAllContrats();

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetContratsByClientEmail() {
        // Création d'un contrat simulé
        Contrat contrat1 = new Contrat();
        contrat1.setClient("test@example.com");
        Contrat contrat2 = new Contrat();
        contrat2.setClient("test@example.com");

        when(contratRepository.findByClient("test@example.com")).thenReturn(List.of(contrat1, contrat2));

        List<Contrat> result = contratServiceImp.getContratsByClientEmail("test@example.com");

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getClient());
        assertEquals("test@example.com", result.get(1).getClient());
    }

    @Test
    void testSavePdfFile_withInvalidFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.savePdfFile(file, 1L)
        );

        assertEquals("Invalid file provided", exception.getMessage());
    }
    @Test
    void testParsePrix_withEmptyString() {
        Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
            Method method = ContratServiceImp.class.getDeclaredMethod("parsePrix", String.class, int.class);
            method.setAccessible(true);
            method.invoke(contratServiceImp, "", 0);
        });

        assertInstanceOf(IllegalArgumentException.class, thrown.getCause());
        assertEquals("Invalid price format", thrown.getCause().getMessage());
    }


    @Test
    void testCreateContract_withNullToken() {
        CreateContractRequest request = new CreateContractRequest();
        request.setAssurerTransport(true);
        request.setAssurerPersonnes(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.createContract(request, null, null)
        );

        assertEquals("Token is required", exception.getMessage());
    }

    @Test
    void testBuildOffreResponse_withUnsupportedDuration() {
        CreateContractRequest request = new CreateContractRequest();
        request.setDureeContrat("unsupported_duration");

        Offre offre = new Offre();
        offre.setPrixMin("100€/personneAccompagnante");
        offre.setPrixMax("150€/personneAccompagnante");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> contratServiceImp.buildOffreResponse(offre, request)
        );

        assertEquals("Durée du contrat invalide", exception.getMessage());
    }

    @Test
    void testRepositoryFailure_inGetContratById() {
        when(contratRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contratServiceImp.getContratById(1L)
        );

        assertEquals("Database error", exception.getMessage());
    }


}
