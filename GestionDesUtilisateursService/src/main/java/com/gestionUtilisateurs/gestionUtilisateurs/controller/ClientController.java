package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.MaladieChroniqueDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController
{
    private final UserServiceItf userService;


    public ClientController(UserServiceItf userService) {
        this.userService = userService;
    }

    @PostMapping("/updateInfosMaladies")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> setMaladies(
            @RequestParam MaladieChroniqueDTO maladieChroniqueDTO) {
        try {
            userService.updateMaladieChronique(maladieChroniqueDTO);
            return ResponseEntity.ok("Date de naissance mise à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour des maladies chroniques : " + e.getMessage());
        }
    }
}
