package com.gestioncontrats.gestioncontrats.entity;

import com.gestioncontrats.gestioncontrats.model.Accompagnant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class AccompagnantEntityTest {

    @Test
    void testAccompagnantEntity() {
        Accompagnant accompagnant = new Accompagnant();

        Long id = 1L;
        String nom = "Dupont";
        String prenom = "Jean";
        String sexe = "M";
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);

        accompagnant.setId(id);
        accompagnant.setNom(nom);
        accompagnant.setPrenom(prenom);
        accompagnant.setSexe(sexe);
        accompagnant.setDateNaissance(dateNaissance);

        assertEquals(id, accompagnant.getId());
        assertEquals(nom, accompagnant.getNom());
        assertEquals(prenom, accompagnant.getPrenom());
        assertEquals(sexe, accompagnant.getSexe());
        assertEquals(dateNaissance, accompagnant.getDateNaissance());
    }
}

