package com.gestionUtilisateurs.gestionUtilisateurs.controller;

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
    public ResponseEntity<Utilisateur> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur currentUser = (Utilisateur) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CONSEILLER')")
    public ResponseEntity<List<Utilisateur>> allUsers() {
        List <Utilisateur> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}
