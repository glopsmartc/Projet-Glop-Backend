package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Logisticien;
import com.gestionUtilisateurs.gestionUtilisateurs.model.LogisticienRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/insert_roles.sql")
class LogisticienEntityTest {

    @Autowired
    private LogisticienRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void testSaveValidLogisticien() {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.LOGISTICIEN);

        Logisticien logisticien = new Logisticien();
        logisticien.setNom("Smith");
        logisticien.setPrenom("Anna");
        logisticien.setEmail("anna.smith@example.com");
        logisticien.setAdresse("5678 Logistics Ave");
        logisticien.setNumTel("+1234567890");
        logisticien.setMotDePasse("securepassword");
        logisticien.setDateNaissance(LocalDate.of(1988, 3, 3));
        logisticien.setRole(optionalRole.get());

        Logisticien savedLogisticien = utilisateurRepository.save(logisticien);

        assertNotNull(savedLogisticien);
        assertNotNull(savedLogisticien.getIdUser());
        assertEquals("Smith", savedLogisticien.getNom());
        assertEquals("Anna", savedLogisticien.getPrenom());
        assertEquals(optionalRole.get(), savedLogisticien.getRole());
    }

    @Test
    void testInvalidLogisticienThrowsException() {
        Logisticien logisticien = new Logisticien();
        logisticien.setNom("");
        logisticien.setPrenom("Anna");
        logisticien.setEmail("anna.smith@example.com");
        logisticien.setAdresse("5678 Logistics Ave");
        logisticien.setNumTel("+1234567890");
        logisticien.setMotDePasse("password");
        logisticien.setDateNaissance(LocalDate.of(1988, 3, 3));

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(logisticien);
        });
    }

    @Test
    void testLogisticienValidation_NullFields() {
        Logisticien logisticien = new Logisticien();
        logisticien.setNom(null);
        logisticien.setPrenom(null);
        logisticien.setEmail(null);

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(logisticien);
        });
    }
}
