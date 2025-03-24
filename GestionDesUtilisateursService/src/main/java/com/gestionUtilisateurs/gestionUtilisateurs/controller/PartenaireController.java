package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;
import com.gestionUtilisateurs.gestionUtilisateurs.service.PartenaireServiceItf;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/partenaires")
public class PartenaireController {

    private final PartenaireServiceItf partenaireServiceItf;

    public PartenaireController(PartenaireServiceItf partenaireServiceItf) {
        this.partenaireServiceItf = partenaireServiceItf;
    }
    
    @GetMapping("/")
    @PreAuthorize("hasRole('CONSEILLER') or hasRole('LOGISTICIEN')")
    public ResponseEntity<List<Partenaire>> allPartenaires() {
        List <Partenaire> partenaires = partenaireServiceItf.allPartenaires();

        return ResponseEntity.ok(partenaires);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CONSEILLER') or hasRole('LOGISTICIEN')")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        Optional<Partenaire> partenaire = partenaireServiceItf.getPartenaireById(id);
        return partenaire.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
