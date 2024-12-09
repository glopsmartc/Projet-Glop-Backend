package com.gestioncontrats.gestioncontrats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.dto.UtilisateurDTO;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import com.gestioncontrats.gestioncontrats.service.ContratServiceItf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequestMapping("/api/contrat")
@RestController
public class ContratController {
    private final ContratServiceItf contratService;

    private final OffreRepository offreRepository;

    private final UserClientService utilisateurService;

    public ContratController(ContratServiceItf contratService, OffreRepository offreRepository, UserClientService utilisateurService) {
        this.contratService = contratService;
        this.offreRepository = offreRepository;
        this.utilisateurService = utilisateurService;
    }

    @PostMapping( value="/create", consumes = {MULTIPART_FORM_DATA_VALUE, "application/json"})
    //@PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> createContract(@RequestHeader("Authorization") String authorizationHeader, @RequestPart("request") String requestJson, @RequestPart(value = "file") MultipartFile pdfFile) throws IOException {
        String token = authorizationHeader.replace("Bearer ", "");

        // Deserialize JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        CreateContractRequest request = mapper.readValue(requestJson, CreateContractRequest.class);

        System.out.println("Received Content-Type: " + pdfFile.getContentType());
        System.out.println("Request Details: " + request);
        
        if (!pdfFile.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Le fichier doit être un PDF.");
        }
        Contrat contrat = contratService.createContract(request, pdfFile, token);
        if (contrat.getOffre() == null) {
            return ResponseEntity.badRequest().body("Aucune offre correspondante trouvée.");
        }
        return new ResponseEntity<>(contrat, HttpStatus.CREATED);
    }


    @PostMapping("/getOffre")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getOffreCorrespondante(@RequestBody CreateContractRequest request) {
        Offre offreCorrespondante = contratService.findMatchingOffre(request);
        if (offreCorrespondante == null) {
            return ResponseEntity.badRequest().body("Aucune offre correspondante trouvée.");
        }
        // Construire la réponse enrichie
        OffreResponse response = contratService.buildOffreResponse(offreCorrespondante, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    //@PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Offre>> allOffres() {
        List<Offre> offres = new ArrayList<>();
        System.out.println("Entered allOffres method");

        offreRepository.findAll().forEach(offres::add);

        if (offres.isEmpty()) {
            System.out.println("No offers found.");
        } else {
            System.out.println("Found offers: " + offres);
        }

        return ResponseEntity.ok(offres);
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentContract() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Nom d'utilisateur authentifié
            return ResponseEntity.ok("L'utilisateur " + username + " a accès au contrat.");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé.");
    }

    @GetMapping("/current-user")
    public UtilisateurDTO getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return utilisateurService.getAuthenticatedUser(token);
    }

    @GetMapping("/user-contracts")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Contrat>> getUserContracts(@RequestHeader("Authorization") String authorizationHeader) {
        // Extraction du token
        String token = authorizationHeader.replace("Bearer ", "");
        // Récupération de l'email à partir du token
        String clientEmail = utilisateurService.getAuthenticatedUser(token).getEmail();

        // Récupération des contrats
        List<Contrat> contrats = contratService.getContratsByClientEmail(clientEmail);

        // Log pour vérifier les contrats récupérés
        System.out.println("Contrats récupérés pour l'email " + clientEmail + ": " + contrats);

        // Retourner les contrats ou un statut 404 si aucun n'est trouvé
        if (contrats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(contrats);
    }

}
