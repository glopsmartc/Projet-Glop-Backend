package com.gestionUtilisateurs.gestionUtilisateurs.repository;

import com.gestionUtilisateurs.gestionUtilisateurs.GestionDesUtilisateursServiceApplication;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GestionDesUtilisateursServiceApplication.class)
@ActiveProfiles("test")
public class UtilisateurRepositoryTest {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Utilisateur utilisateur;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setNom("Dupont");
        utilisateur.setPrenom("Jean");
        utilisateur.setDateNaissance(LocalDate.of(1980, 1, 1));
        utilisateur.setAdresse("123 Rue Exemple");
        utilisateur.setEmail("jean.dupont@example.com");
        utilisateur.setNumTel("+33612345678");
        utilisateur.setMotDePasse("motdepasse123");
    }

    @Test
    public void testFindByEmail_NotFound() {
        Optional<Utilisateur> foundUtilisateur = utilisateurRepository.findByEmail("nonexistent.email@example.com");
        assertFalse(foundUtilisateur.isPresent());
    }

    @Test
    public void testFindByResetToken_NotFound() {
        Optional<Utilisateur> foundUtilisateur = utilisateurRepository.findByResetToken("nonexistent-token");
        assertFalse(foundUtilisateur.isPresent());
    }
}
