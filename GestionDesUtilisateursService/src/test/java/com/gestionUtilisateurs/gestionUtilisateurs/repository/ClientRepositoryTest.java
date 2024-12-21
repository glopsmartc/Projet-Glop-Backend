package com.gestionUtilisateurs.gestionUtilisateurs.repository;

import com.gestionUtilisateurs.gestionUtilisateurs.GestionDesUtilisateursServiceApplication;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ClientRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GestionDesUtilisateursServiceApplication.class)
@ActiveProfiles("test")
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setNumTel("0123456789");
        client.setDateNaissance(LocalDate.of(1990, 1, 1));
        client.setMotDePasse("securePassword123");
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setAdresse("123 Rue Exemple");
    }

    @Test
    public void testClientValidation() {
        Client invalidClient = new Client();
        invalidClient.setNumTel(null);
        invalidClient.setDateNaissance(null);
        invalidClient.setMotDePasse(null);
        invalidClient.setNom("");
        invalidClient.setPrenom(null);
        invalidClient.setAdresse("");

        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(invalidClient);
        });
    }
}
