package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ClientRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/insert_roles.sql")
public class ClientEntityTest {

    @Autowired
    private ClientRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Client utilisateur;

    @BeforeEach
    public void setUp() {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CLIENT);
        utilisateur = new Client();
        utilisateur.setNom("John");
        utilisateur.setPrenom("Doe");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setMotDePasse("strongpassword");
        utilisateur.setAdresse("123 Rue Exemple");
        utilisateur.setDateNaissance(LocalDate.of(1990, 1, 1));
        utilisateur.setNumTel("0123456789");
        utilisateur.setRole(optionalRole.get());
    }

    @Test
    public void testSaveValidClient() {
        Client savedUtilisateur = utilisateurRepository.save(utilisateur);
        assertNotNull(savedUtilisateur.getIdUser());
        assertEquals(utilisateur.getNom(), savedUtilisateur.getNom());
        assertEquals(utilisateur.getPrenom(), savedUtilisateur.getPrenom());
        assertEquals(utilisateur.getEmail(), savedUtilisateur.getEmail());
    }

    @Test
    public void testFindClientById() {
        Client savedUtilisateur = utilisateurRepository.save(utilisateur);
        Client foundUtilisateur = utilisateurRepository.findById(savedUtilisateur.getIdUser()).orElse(null);

        assertNotNull(foundUtilisateur);
        assertEquals(savedUtilisateur.getNom(), foundUtilisateur.getNom());
    }

    @Test
    public void testDeleteClient() {
        Client savedUtilisateur = utilisateurRepository.save(utilisateur);
        utilisateurRepository.delete(savedUtilisateur);

        assertEquals(utilisateurRepository.findById(savedUtilisateur.getIdUser()).isPresent(), false);
    }

    @Test
    void testClientValidation_NullFields() {
        Client client = new Client();
        client.setNom(null);
        client.setPrenom(null);
        client.setEmail(null);

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(client);
        });
    }
}
