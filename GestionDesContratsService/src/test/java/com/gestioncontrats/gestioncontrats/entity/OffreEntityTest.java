package com.gestioncontrats.gestioncontrats.entity;

import com.gestioncontrats.gestioncontrats.model.Offre;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OffreEntityTest {

    @Test
    void testOffreEntity() {
        String nomOffre = "Offre Premium";
        String descriptionMin = "Description courte de l'offre premium.";
        String descriptionMax = "Description détaillée de l'offre premium avec toutes les fonctionnalités.";
        String prixMin = "100";
        String prixMax = "500";

        Offre offre = new Offre(nomOffre, descriptionMin, descriptionMax, prixMin, prixMax);

        assertEquals(nomOffre, offre.getNomOffre());
        assertEquals(descriptionMin, offre.getDescriptionMin());
        assertEquals(descriptionMax, offre.getDescriptionMax());
        assertEquals(prixMin, offre.getPrixMin());
        assertEquals(prixMax, offre.getPrixMax());

        offre.setNomOffre("Offre Basique");
        offre.setDescriptionMin("Nouvelle description courte.");
        offre.setDescriptionMax("Nouvelle description détaillée.");
        offre.setPrixMin("50");
        offre.setPrixMax("300");

        assertEquals("Offre Basique", offre.getNomOffre());
        assertEquals("Nouvelle description courte.", offre.getDescriptionMin());
        assertEquals("Nouvelle description détaillée.", offre.getDescriptionMax());
        assertEquals("50", offre.getPrixMin());
        assertEquals("300", offre.getPrixMax());
    }
}

