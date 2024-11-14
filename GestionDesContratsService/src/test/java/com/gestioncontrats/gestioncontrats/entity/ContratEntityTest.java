package com.gestioncontrats.gestioncontrats.entity;

import com.gestioncontrats.gestioncontrats.model.Accompagnant;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class ContratEntityTest {

    @Test
    void testContratEntity() {
        Contrat contrat = new Contrat();

        Long id = 1L;
        String dureeContrat = "12 mois";
        boolean assurerTransport = true;
        boolean assurerPersonnes = false;
        boolean voiture = true;
        boolean trotinette = false;
        boolean bicyclette = true;
        Integer nombrePersonnes = 2;
        LocalDate debutContrat = LocalDate.of(2023, 1, 1);
        String destination = "Paris";
        LocalDate dateAller = LocalDate.of(2023, 5, 10);
        LocalDate dateRetour = LocalDate.of(2023, 5, 20);
        String numeroTelephone = "0123456789";
        LocalDate dateNaissanceSouscripteur = LocalDate.of(1990, 1, 1);

        Accompagnant accompagnant1 = new Accompagnant();
        accompagnant1.setNom("Dupont");
        accompagnant1.setPrenom("Jean");

        Accompagnant accompagnant2 = new Accompagnant();
        accompagnant2.setNom("Martin");
        accompagnant2.setPrenom("Marie");

        List<Accompagnant> accompagnants = Arrays.asList(accompagnant1, accompagnant2);

        Offre offre = new Offre();
        offre.setNomOffre("Assurance Complète");

        contrat.setId(id);
        contrat.setDureeContrat(dureeContrat);
        contrat.setAssurerTransport(assurerTransport);
        contrat.setAssurerPersonnes(assurerPersonnes);
        contrat.setVoiture(voiture);
        contrat.setTrotinette(trotinette);
        contrat.setBicyclette(bicyclette);
        contrat.setNombrePersonnes(nombrePersonnes);
        contrat.setDebutContrat(debutContrat);
        contrat.setDestination(destination);
        contrat.setDateAller(dateAller);
        contrat.setDateRetour(dateRetour);
        contrat.setNumeroTelephone(numeroTelephone);
        contrat.setDateNaissanceSouscripteur(dateNaissanceSouscripteur);
        contrat.setAccompagnants(accompagnants);
        contrat.setOffre(offre);

        assertEquals(id, contrat.getId());
        assertEquals(dureeContrat, contrat.getDureeContrat());
        assertEquals(assurerTransport, contrat.isAssurerTransport());
        assertEquals(assurerPersonnes, contrat.isAssurerPersonnes());
        assertEquals(voiture, contrat.isVoiture());
        assertEquals(trotinette, contrat.isTrotinette());
        assertEquals(bicyclette, contrat.isBicyclette());
        assertEquals(nombrePersonnes, contrat.getNombrePersonnes());
        assertEquals(debutContrat, contrat.getDebutContrat());
        assertEquals(destination, contrat.getDestination());
        assertEquals(dateAller, contrat.getDateAller());
        assertEquals(dateRetour, contrat.getDateRetour());
        assertEquals(numeroTelephone, contrat.getNumeroTelephone());
        assertEquals(dateNaissanceSouscripteur, contrat.getDateNaissanceSouscripteur());
        assertEquals(accompagnants, contrat.getAccompagnants());
        assertEquals(offre, contrat.getOffre());
    }
}
