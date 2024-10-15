package com.gestionUtilisateurs.gestionUtilisateurs.controller;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.service.UserServiceItf;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conseillers")
public class ConseillerController {

    private final UserServiceItf userService;

    public ConseillerController(UserServiceItf userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CONSEILLER')")
    public ResponseEntity<Utilisateur> createConseiller(@RequestBody RegisterUserDto registerUserDto) {
        Utilisateur createdAdmin = userService.createConseiller(registerUserDto);

        return ResponseEntity.ok(createdAdmin);
    }
}
