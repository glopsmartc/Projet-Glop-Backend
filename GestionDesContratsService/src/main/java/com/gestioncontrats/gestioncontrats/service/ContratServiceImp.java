package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratServiceImp implements ContratServiceItf {
    private final ContratRepository contratRepository;
    private final OffreRepository offreRepository;

    private final UserClientService userClientService;

    public ContratServiceImp(ContratRepository contratRepository, OffreRepository offreRepository, UserClientService userClientService) {
        this.contratRepository = contratRepository;
        this.offreRepository = offreRepository;
        this.userClientService = userClientService;
    }

    @Override
    public Contrat createContract(CreateContractRequest request, MultipartFile pdfFile, String token) throws IOException  {
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
        contrat.setPrice(request.getPrice());
        contrat.setClient(userClientService.getAuthenticatedUser(token).getEmail());
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
        Offre offreCorrespondante = offreRepository.findByNomOffre(request.getPlanName());
        if (offreCorrespondante == null) {
            throw new IllegalArgumentException("Aucune offre correspondante trouvée.");
        }
        contrat.setOffre(offreCorrespondante);

        // Sauvegarder le fichier PDF
        String pdfPath = savePdfFile(pdfFile);

        // Associer le chemin du PDF au contrat
        contrat.setPdfPath(pdfPath);

        // Sauvegarder le contrat dans la base de données
        return contratRepository.save(contrat);
    }

    @Override
    public OffreResponse buildOffreResponse(Offre offre, CreateContractRequest request) {
        int nombrePersonnes = request.getNombrePersonnes() != null ? request.getNombrePersonnes() : 0;

        // Récupération des prix et gestion du suffixe "/personneAccompagnante"
        double prixMin = parsePrix(offre.getPrixMin(), nombrePersonnes);
        double prixMax = parsePrix(offre.getPrixMax(), nombrePersonnes);

        double prixMinTotal = prixMin;
        double prixMaxTotal = prixMax;

        // Appliquer le calcul en fonction de la durée du contrat
        double ajustementPrix = 1.0;
        switch (request.getDureeContrat()) {
            case "1_mois":
                ajustementPrix = 1.0; // Pas de modification pour 1 mois
                break;
            case "3_mois":
                ajustementPrix = 3.0; // Multiplication par 3 pour 3 mois
                break;
            case "4_mois":
                ajustementPrix = 4.0; // Multiplication par 4 pour 4 mois
                break;
            case "1_an":
                ajustementPrix = 12.0; // Multiplication par 12 pour 1 an
                break;
            case "1_voyage":
                if (request.getDateAller() != null && request.getDateRetour() != null) {
                    long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(request.getDateAller(), request.getDateRetour());
                    ajustementPrix = daysBetween / 30.0; // Calcul de la durée en mois
                }
                break;
            default:
                throw new IllegalArgumentException("Durée du contrat invalide");
        }

        // Appliquer l'ajustement sur les prix
        prixMinTotal *= ajustementPrix;
        prixMaxTotal *= ajustementPrix;

        OffreResponse response = new OffreResponse();
        response.setNomOffre(offre.getNomOffre());
        response.setDescriptionMin(offre.getDescriptionMin());
        response.setDescriptionMax(offre.getDescriptionMax());
        response.setPrixMinTotal(String.format("%.2f€", prixMinTotal));
        response.setPrixMaxTotal(String.format("%.2f€", prixMaxTotal));

        return response;
    }

    // Méthode utilitaire pour extraire le prix
    private double parsePrix(String prix, int nombrePersonnes) {
        System.out.println("Prix avant parsing : " + prix);
        System.out.println("Nombre de personnes : " + nombrePersonnes);
        if (prix == null || prix.isEmpty()) {
            return 0.0;
        }

        String prixNet = prix.replaceAll("[^\\d.]", "");
        double prixDouble = Double.parseDouble(prixNet);

        if (prix.contains("/personneAccompagnante")) {
            return prixDouble * (nombrePersonnes > 0 ? nombrePersonnes : 1);
        }
        return prixDouble;
    }

    @Override
    public Offre findMatchingOffre(CreateContractRequest request) {
        List<Offre> offres = (List<Offre>) offreRepository.findAll();
        for (Offre offre : offres) {
            boolean match = true;

            // Logique de correspondance basée sur les critères
            if (request.isAssurerTransport() && !offre.getNomOffre().contains("AvecMoyTra")) {
                match = false;
            }
            if (request.isAssurerPersonnes() && !offre.getNomOffre().contains("AvecAcc")) {
                match = false;
            }

            if (match) {
                return offre;
            }
        }
        return null; // Aucune offre trouvée
    }

    // sauvegarder le pdf sur le disque
    public String savePdfFile(MultipartFile file) throws IOException {
        // Get the user's Documents directory
        String documentsPath = System.getProperty("user.home") + File.separator + "Documents";

        // Ensure the directory exists
        File directory = new File(documentsPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the full file path
        File destinationFile = new File(documentsPath + File.separator + file.getOriginalFilename());

        // Save the file
        file.transferTo(destinationFile);
        System.out.println("File saved to: " + destinationFile.getAbsolutePath());
    return destinationFile.getAbsolutePath();
    }
}
