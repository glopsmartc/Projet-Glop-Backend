package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Logisticien;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LogisticienEntityTest {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveValidLogisticien() {
        Role role = new Role();
        role.setName(RoleEnum.LOGISTICIEN);
        role.setDescription("Role logisticien");
        roleRepository.save(role);

        Logisticien logisticien = new Logisticien();
        logisticien.setNom("Smith");
        logisticien.setPrenom("Anna");
        logisticien.setEmail("anna.smith@example.com");
        logisticien.setAdresse("5678 Logistics Ave");
        logisticien.setNumTel("+1234567890");
        logisticien.setMotDePasse("securepassword");
        logisticien.setDateNaissance(LocalDate.of(1988, 3, 3));
        logisticien.setRole(role);

        Logisticien savedLogisticien = utilisateurRepository.save(logisticien);

        assertNotNull(savedLogisticien);
        assertNotNull(savedLogisticien.getIdUser());
        assertEquals("Smith", savedLogisticien.getNom());
        assertEquals("Anna", savedLogisticien.getPrenom());
        assertEquals(role, savedLogisticien.getRole());
    }
}
