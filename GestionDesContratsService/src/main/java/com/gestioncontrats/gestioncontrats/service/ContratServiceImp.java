package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratServiceImp implements ContratServiceItf {

    private final ContratRepository contratRepository;
    private final OffreRepository offreRepository;

    public ContratServiceImp(ContratRepository contratRepository, OffreRepository offreRepository) {
        this.contratRepository = contratRepository;
        this.offreRepository = offreRepository;
    }

    @Override
    public Contrat createContract(CreateContractRequest request) {
        Contrat contrat = new Contrat();
        contrat.setDureeContrat(request.getDureeContrat());
        contrat.setAssurerTransport(request.isAssurerTransport());
        contrat.setAssurerPersonnes(request.isAssurerPersonnes());
        contrat.setVoiture(request.isVoiture());
        contrat.setTrotinette(request.isTrotinette());
        contrat.setBicyclette(request.isBicyclette());
        contrat.setNombrePersonnes(request.getNombrePersonnes());
        contrat.setDebutContrat(request.getDebutContrat());
        contrat.setDestination(request.getDestination());
        contrat.setDateAller(request.getDateAller());
        contrat.setDateRetour(request.getDateRetour());
        contrat.setNumeroTelephone(request.getNumeroTelephone());
        contrat.setDateNaissanceSouscripteur(request.getDateNaissanceSouscripteur());

        // Convertir les accompagnants
        contrat.setAccompagnants(request.getAccompagnants().stream().map(dto -> {
            Accompagnant accompagnant = new Accompagnant();
            accompagnant.setNom(dto.getNom());
            accompagnant.setPrenom(dto.getPrenom());
            accompagnant.setSexe(dto.getSexe());
            accompagnant.setDateNaissance(dto.getDateNaissance());
            return accompagnant;
        }).collect(Collectors.toList()));

        // Trouver l'offre correspondante
        Offre offreCorrespondante = findMatchingOffre(request);
        contrat.setOffre(offreCorrespondante);

        // Sauvegarder le contrat dans la base de données
        return contratRepository.save(contrat);
    }

    @Override
    public Offre findMatchingOffre(CreateContractRequest request) {
        List<Offre> offres = (List<Offre>) offreRepository.findAll();
        for (Offre offre : offres) {
            boolean match = true;

            // Logique de correspondance basée sur les critères du formulaire
            if (request.isAssurerTransport() && !offre.getNomOffre().contains("AvecMoyTra")) {
                match = false;
            }
            if (request.isAssurerPersonnes() && !offre.getNomOffre().contains("AvecAcc")) {
                match = false;
            }
            // Ajouter d'autres conditions si nécessaires

            if (match) {
                return offre;
            }
        }
        return null; // Si aucune offre ne correspond
    }
}
