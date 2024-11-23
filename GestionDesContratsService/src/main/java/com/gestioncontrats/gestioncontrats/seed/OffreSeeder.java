package com.gestioncontrats.gestioncontrats.seed;

import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OffreSeeder implements CommandLineRunner {

    private final OffreRepository offreRepository;

    public OffreSeeder(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Offre offre1 = new Offre(
                "SansAcc-SansMoyTra",
                "Conseils médicaux\nOrientation vers des hôpitaux référencés",
                "Conseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement",
                "20€/mois",
                "50€/mois"
        );

        Offre offre2 = new Offre(
                "SansAcc-AvecMoyTra",
                "Dépannage\nRemorquage\nConseils médicaux\nOrientation vers des hôpitaux référencés",
                "Dépannage\nRemorquage\nAssistance technique et diagnostique\nConseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement",
                "40€/mois",
                "60€/mois"
        );

        Offre offre3 = new Offre(
                "AvecAcc-SansMoyTra",
                "Conseils médicaux\nOrientation vers des hôpitaux référencés",
                "Conseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement",
                "25€/mois/personneAccompagnante",
                "70€/mois/personneAccompagnante"
        );

        Offre offre4 = new Offre(
                "AvecAcc-AvecMoyTra",
                "Dépannage\nRemorquage\nConseils médicaux\nOrientation vers des hôpitaux référencés",
                "Dépannage\nRemorquage\nAssistance technique et diagnostique\nConseils médicaux\nOrientation vers des hôpitaux référencés\nPrise en charge des frais médicaux\nRapatriement",
                "65€/mois/personneAccompagnante",
                "130€/mois/personneAccompagnante"
        );
        offreRepository.save(offre1);
        offreRepository.save(offre2);
        offreRepository.save(offre3);
        offreRepository.save(offre4);

        System.out.println("Offres insérées avec succès !");
    }
}
