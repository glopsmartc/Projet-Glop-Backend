package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.UtilisateurDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UtilisateurController {
    private final UserServiceItf userService;

    public UtilisateurController(UserServiceItf userService) {
        this.userService = userService;
    }

    @GetMapping("/current-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UtilisateurDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setRole(utilisateur.getRole().getName().toString());
        utilisateurDTO.setUsername(utilisateur.getUsername());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setNom(utilisateur.getNom());

        return ResponseEntity.ok(utilisateurDTO);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CONSEILLER')")
    public ResponseEntity<List<Utilisateur>> allUsers() {
        List <Utilisateur> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/setDateNaissance")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> setDateNaissance(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance) {
        try {
            if (email == null || dateNaissance == null) {
                return ResponseEntity.badRequest().body("Email ou date de naissance manquants.");
            }

            // Appel du service pour mettre à jour l'utilisateur
            userService.updateDateNaissance(email, dateNaissance);

            return ResponseEntity.ok("Date de naissance mise à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour de la date de naissance : " + e.getMessage());
        }
    }




}
