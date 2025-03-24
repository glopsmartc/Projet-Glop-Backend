package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.MaladieChroniqueDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.service.ClientServiceItf;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController
{
    private final UserServiceItf userService;
    private final ClientServiceItf clientServiceItf;

    public ClientController(UserServiceItf userService, ClientServiceItf clientServiceItf) {
        this.userService = userService;
        this.clientServiceItf = clientServiceItf;
    }

    @PostMapping("/updateInfosMaladies")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> setMaladies(
            @RequestBody MaladieChroniqueDTO maladieChroniqueDTO) {
        try {
            userService.updateMaladieChronique(maladieChroniqueDTO);
            return ResponseEntity.ok("Maladies chroniques mise à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour des maladies chroniques : " + e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CONSEILLER')")
    public ResponseEntity<List<Client>> allClients() {
        List <Client> clients = clientServiceItf.allClients();

        return ResponseEntity.ok(clients);
    }
}
