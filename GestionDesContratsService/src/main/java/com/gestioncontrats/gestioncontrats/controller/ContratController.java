package com.gestioncontrats.gestioncontrats.controller;

import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.service.ContratServiceItf;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/contrat")
@RestController
public class ContratController {
    private final ContratServiceItf contratService;

    public ContratController(ContratServiceItf contratService) {
        this.contratService = contratService;
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

    @PostMapping("/getOffre")
    public ResponseEntity<?> getOffreCorrespondante(@RequestBody CreateContractRequest request) {
        Offre offreCorrespondante = contratService.findMatchingOffre(request);

        if (offreCorrespondante == null) {
            return ResponseEntity.badRequest().body("Aucune offre correspondante trouvée.");
        }

        // Retourner uniquement l'offre
        return ResponseEntity.ok(offreCorrespondante);
    }
}
