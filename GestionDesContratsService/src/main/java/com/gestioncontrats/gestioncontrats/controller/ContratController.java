package com.gestioncontrats.gestioncontrats.controller;

import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
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

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/create")
    public ResponseEntity<?> createContract(@RequestBody CreateContractRequest request) {
        Contrat contrat = contratService.createContract(request);

        if (contrat.getOffre() == null) {
            return ResponseEntity.badRequest().body("Aucune offre correspondante trouvée.");
        }

        // Retourne le contrat avec l'offre correspondante
        return ResponseEntity.ok(contrat);
    }

    @GetMapping("/getOffre")
    public ResponseEntity<?> getOffreCorrespondante(@RequestBody CreateContractRequest request) {
        Offre offreCorrespondante = contratService.findMatchingOffre(request);
        if (offreCorrespondante == null) {
            return ResponseEntity.badRequest().body("Aucune offre correspondante trouvée.");
        }

        // Retourner uniquement l'offre
        return ResponseEntity.ok(offreCorrespondante);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT')")
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
}
