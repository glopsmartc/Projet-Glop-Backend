package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Conseiller;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ConseillerRepository;
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
class ConseillerEntityTest {

    @Autowired
    private ConseillerRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveValidConseiller() {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CONSEILLER);

        Conseiller conseiller = new Conseiller();
        conseiller.setNom("Doe");
        conseiller.setPrenom("Jane");
        conseiller.setEmail("jane.doe@example.com");
        conseiller.setAdresse("4321 Another St");
        conseiller.setNumTel("+0987654321");
        conseiller.setMotDePasse("securepassword");
        conseiller.setDateNaissance(LocalDate.of(1992, 2, 2));
        conseiller.setRole(optionalRole.get());

        Conseiller savedConseiller = utilisateurRepository.save(conseiller);

        assertNotNull(savedConseiller);
        assertNotNull(savedConseiller.getIdUser());
        assertEquals("Doe", savedConseiller.getNom());
        assertEquals("Jane", savedConseiller.getPrenom());
        assertEquals(optionalRole.get(), savedConseiller.getRole());
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
