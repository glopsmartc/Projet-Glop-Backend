package com.gestionUtilisateurs.gestionUtilisateurs.Entity;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@DataJpaTest
@Transactional
public class ClientEntityTest {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Utilisateur utilisateur;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setNom("John");
        utilisateur.setPrenom("Doe");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setMotDePasse("strongpassword");
        utilisateur.setAdresse("123 Rue Exemple");
        utilisateur.setDateNaissance(LocalDate.of(1990, 1, 1));
        utilisateur.setNumTel("0123456789");
    }

    @Test
    public void testSaveValidClient() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        assertNotNull(savedUtilisateur.getIdUser());
        assertEquals(utilisateur.getNom(), savedUtilisateur.getNom());
        assertEquals(utilisateur.getPrenom(), savedUtilisateur.getPrenom());
        assertEquals(utilisateur.getEmail(), savedUtilisateur.getEmail());
    }

    @Test
    public void testFindClientById() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        Utilisateur foundUtilisateur = utilisateurRepository.findById(savedUtilisateur.getIdUser()).orElse(null);

        assertNotNull(foundUtilisateur);
        assertEquals(savedUtilisateur.getNom(), foundUtilisateur.getNom());
    }


}
