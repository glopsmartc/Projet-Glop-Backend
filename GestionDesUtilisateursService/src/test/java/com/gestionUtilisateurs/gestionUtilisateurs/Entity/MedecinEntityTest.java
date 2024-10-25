package com.gestionUtilisateurs.gestionUtilisateurs.Entity;


import com.gestionUtilisateurs.gestionUtilisateurs.model.Medecin;
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
class MedecinEntityTest {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveValidMedecin() {
        Role role = new Role();
        role.setName(RoleEnum.MEDECIN);
        role.setDescription("Role medecin");
        roleRepository.save(role);

        Medecin medecin = new Medecin();
        medecin.setNom("Brown");
        medecin.setPrenom("Tom");
        medecin.setEmail("tom.brown@example.com");
        medecin.setAdresse("1357 Health St");
        medecin.setNumTel("+1234567890");
        medecin.setMotDePasse("securepassword");
        medecin.setDateNaissance(LocalDate.of(1985, 4, 4));
        medecin.setRole(role);
        medecin.setSpecialite("Cardiology");
        medecin.setDisponibilites(new String[]{"Mon 9-11", "Tue 10-12"});
        medecin.setDeplacement(true);

        Medecin savedMedecin = utilisateurRepository.save(medecin);

        assertNotNull(savedMedecin);
        assertNotNull(savedMedecin.getIdUser());
        assertEquals("Brown", savedMedecin.getNom());
        assertEquals("Tom", savedMedecin.getPrenom());
        assertEquals(role, savedMedecin.getRole());
        assertEquals("Cardiology", savedMedecin.getSpecialite());
        assertArrayEquals(new String[]{"Mon 9-11", "Tue 10-12"}, savedMedecin.getDisponibilites());
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
}

