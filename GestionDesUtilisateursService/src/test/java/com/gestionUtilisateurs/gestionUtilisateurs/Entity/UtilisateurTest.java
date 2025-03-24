package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    private Utilisateur utilisateur;
    private Role role;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setNom("Doe");
        utilisateur.setPrenom("John");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setMotDePasse("password123");
        utilisateur.setDateNaissance(LocalDate.of(1990, 1, 1));
        utilisateur.setAdresse("123 Rue Exemple");
        utilisateur.setNumTel("+33123456789");

        role = new Role();
        role.setName(RoleEnum.USER);
        utilisateur.setRole(role);
    }

    @Test
    void testGetPassword() {
        String password = utilisateur.getPassword();

        assertEquals("password123", password);
    }

    @Test
    void testGetUsername() {
        String username = utilisateur.getUsername();

        assertEquals("john.doe@example.com", username);
    }

    @Test
    void testIsAccountNonExpired() {
        boolean accountNonExpired = utilisateur.isAccountNonExpired();

        assertTrue(accountNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {

        boolean accountNonLocked = utilisateur.isAccountNonLocked();

        assertTrue(accountNonLocked);
    }

    @Test
    void testIsCredentialsNonExpired() {
        boolean credentialsNonExpired = utilisateur.isCredentialsNonExpired();

        assertTrue(credentialsNonExpired);
    }

    @Test
    void testIsEnabled() {
        boolean enabled = utilisateur.isEnabled();

        assertTrue(enabled);
    }
}