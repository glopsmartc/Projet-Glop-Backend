package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.UtilisateurDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
