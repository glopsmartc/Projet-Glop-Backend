package com.gestionUtilisateurs.gestionUtilisateurs.Entity;


import com.gestionUtilisateurs.gestionUtilisateurs.model.Medecin;
import com.gestionUtilisateurs.gestionUtilisateurs.model.MedecinRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/insert_roles.sql")
class MedecinEntityTest {

    @Autowired
    private MedecinRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveValidMedecin() {

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.LOGISTICIEN);

        Medecin medecin = new Medecin();
        medecin.setNom("Brown");
        medecin.setPrenom("Tom");
        medecin.setEmail("tom.brown@example.com");
        medecin.setAdresse("1357 Health St");
        medecin.setNumTel("+1234567890");
        medecin.setMotDePasse("securepassword");
        medecin.setDateNaissance(LocalDate.of(1985, 4, 4));
        medecin.setRole(optionalRole.get());
        medecin.setSpecialite("Cardiology");
        medecin.setDisponibilites(List.of("Mon 9-11", "Tue 10-12"));
        medecin.setDeplacement(true);

        Medecin savedMedecin = utilisateurRepository.save(medecin);

        assertNotNull(savedMedecin);
        assertNotNull(savedMedecin.getIdUser());
        assertEquals("Brown", savedMedecin.getNom());
        assertEquals("Tom", savedMedecin.getPrenom());
        assertEquals(optionalRole.get(), savedMedecin.getRole());
        assertEquals("Cardiology", savedMedecin.getSpecialite());
        assertEquals(List.of("Mon 9-11", "Tue 10-12"), savedMedecin.getDisponibilites());
        assertTrue(savedMedecin.isDeplacement());
    }
    @Test
    void testInvalidMedecinThrowsException() {
        Medecin medecin = new Medecin();
        medecin.setNom("");
        medecin.setPrenom("Tom");
        medecin.setEmail("tom.brown@example.com");
        medecin.setAdresse("1357 Health St");
        medecin.setNumTel("+1234567890");
        medecin.setMotDePasse("password");
        medecin.setDateNaissance(LocalDate.of(1985, 4, 4));

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(medecin);
        });
    }

    @Test
    void testMedecinValidation_NullFields() {
        Medecin medecin = new Medecin();
        medecin.setNom(null);
        medecin.setPrenom(null);
        medecin.setEmail(null);

        assertThrows(ConstraintViolationException.class, () -> {
            utilisateurRepository.save(medecin);
        });
    }
}

