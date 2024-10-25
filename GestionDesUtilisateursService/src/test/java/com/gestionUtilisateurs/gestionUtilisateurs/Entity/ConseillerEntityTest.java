package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Conseiller;
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
class ConseillerEntityTest {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveValidConseiller() {
        Role role = new Role();
        role.setName(RoleEnum.CONSEILLER);
        role.setDescription("Role conseiller");
        roleRepository.save(role);

        Conseiller conseiller = new Conseiller();
        conseiller.setNom("Doe");
        conseiller.setPrenom("Jane");
        conseiller.setEmail("jane.doe@example.com");
        conseiller.setAdresse("4321 Another St");
        conseiller.setNumTel("+0987654321");
        conseiller.setMotDePasse("securepassword");
        conseiller.setDateNaissance(LocalDate.of(1992, 2, 2));
        conseiller.setRole(role);

        Conseiller savedConseiller = utilisateurRepository.save(conseiller);

        assertNotNull(savedConseiller);
        assertNotNull(savedConseiller.getIdUser());
        assertEquals("Doe", savedConseiller.getNom());
        assertEquals("Jane", savedConseiller.getPrenom());
        assertEquals(role, savedConseiller.getRole());
    }

    @Test
    void testInvalidConseillerThrowsException() {
        Conseiller conseiller = new Conseiller();
        conseiller.setNom("");
        conseiller.setPrenom("Jane");
        conseiller.setEmail("jane.doe@example.com");
        conseiller.setAdresse("4321 Another St");
        conseiller.setNumTel("+0987654321");
        conseiller.setMotDePasse("password");
        conseiller.setDateNaissance(LocalDate.of(1992, 2, 2));

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(conseiller);
        });
    }
}
