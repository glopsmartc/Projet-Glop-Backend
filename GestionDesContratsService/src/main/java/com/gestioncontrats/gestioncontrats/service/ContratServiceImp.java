package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.config.UserClientService;
import com.gestioncontrats.gestioncontrats.controller.ContratController;
import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ContratServiceImp implements ContratServiceItf {
    @Value("${app.storage.path}")
    private String storagePath;

    private final ContratRepository contratRepository;
    private final OffreRepository offreRepository;

    private final UserClientService userClientService;

    private static final Logger log = LoggerFactory.getLogger(ContratController.class);

    public ContratServiceImp(ContratRepository contratRepository, OffreRepository offreRepository, UserClientService userClientService) {
        this.contratRepository = contratRepository;
        this.offreRepository = offreRepository;
        this.userClientService = userClientService;
    }

    @Override
    public Contrat saveContrat(Contrat contrat) {
        if (contrat == null) {
            throw new IllegalArgumentException("Le contrat ne peut pas être null");
        }
        return contratRepository.save(contrat);
    }


    @Override
    public Contrat createContract(CreateContractRequest request, MultipartFile pdfFile, String token) throws IOException {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is required");
        }

        // Create the contract object
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
        contrat.setClientNom(userClientService.getAuthenticatedUser(token).getNom());
        contrat.setClientPrenom(userClientService.getAuthenticatedUser(token).getPrenom());
        contrat.setDescription(request.getDescriptionOffre());
        contrat.setStatut("actif");

        // Convert accompanying persons
        contrat.setAccompagnants(request.getAccompagnants().stream().map(dto -> {
            Accompagnant accompagnant = new Accompagnant();
            accompagnant.setNom(dto.getNom());
            accompagnant.setPrenom(dto.getPrenom());
            accompagnant.setSexe(dto.getSexe());
            accompagnant.setDateNaissance(dto.getDateNaissance());
            return accompagnant;
        }).collect(Collectors.toList()));

        // Find the corresponding offer
        Offre offreCorrespondante = offreRepository.findByNomOffre(request.getPlanName());
        if (offreCorrespondante == null) {
            throw new IllegalArgumentException("Aucune offre correspondante trouvée.");
        }
        contrat.setOffre(offreCorrespondante);

        // Vérifier si le client a souscrit à l'offre "descriptionMin" ou "descriptionMax"
        if (offreCorrespondante.getDescriptionMin().equals(request.getDescriptionOffre())) {
            contrat.setOffreMinOuMax(2);  // Souscrit à l'offre min
        } else {
            contrat.setOffreMinOuMax(1);  // Sinon max
        }

        // Save the contract first to generate its ID
        Contrat savedContrat = contratRepository.save(contrat);

        String pdfPath = savePdfFile(pdfFile, savedContrat.getId());

        // Set the PDF path in the saved contract
        savedContrat.setPdfPath(pdfPath);

        // Return the contract with the PDF path saved
        return contratRepository.save(savedContrat);
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
            case "6_mois":
                ajustementPrix = 6.0; // Multiplication par 6 pour 6 mois
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
        log.info("Prix avant parsing : {}", prix);
        log.info("Nombre de personnes : {}", nombrePersonnes);
        if (prix == null || prix.isEmpty()) {
            throw new IllegalArgumentException("Invalid price format");
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
    public String savePdfFile(MultipartFile file, Long id) throws IOException {
        // Validate input
        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("Invalid file provided");
        }

        // Create the filename with the contract ID and the original file name
        String fileName = id + "_" + file.getOriginalFilename();

        File directory = new File(storagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(storagePath, fileName).normalize();

        Path targetPath = Paths.get(storagePath).toAbsolutePath().normalize();
        if (!filePath.startsWith(targetPath)) {
            throw new IOException("Invalid file path. Path traversal is not allowed.");
        }

        File destinationFile = filePath.toFile();

        // Save the file
        file.transferTo(destinationFile);
        log.info("File saved to: {}", destinationFile.getAbsolutePath());

        return destinationFile.getAbsolutePath();
    }

    @Override
    public List<Offre> getAllOffres() {
        return (List<Offre>) offreRepository.findAll();
    }

    @Override
    public List<Contrat> getAllContrats() {
        return (List<Contrat>) contratRepository.findAll();
    }

    @Override
    public Optional<Contrat> getContratById(Long id) {
        return contratRepository.findById(id);
    }

    @Override
    public List<Contrat> getContratsByClientEmail(String email) {
        return contratRepository.findByClient(email);
    }

    @Override
    public File getContractPdf(Long contratId) {
        Contrat contrat = contratRepository.findById(contratId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat non trouvé."));

        if (contrat.getPdfPath() == null || contrat.getPdfPath().isEmpty()) {
            throw new IllegalArgumentException("Aucun fichier PDF associé à ce contrat.");
        }

        File pdfFile = new File(contrat.getPdfPath());
        if (!pdfFile.exists()) {
            throw new IllegalArgumentException("Le fichier PDF n'existe pas sur le disque.");
        }

        return pdfFile;
    }
    private void updateExpiredContracts() {
        List<Contrat> contratsActifs = StreamSupport.stream(contratRepository.findAll().spliterator(), false)
                .filter(contrat -> "actif".equals(contrat.getStatut()))
                .toList();

        LocalDate today = LocalDate.now();

        for (Contrat contrat : contratsActifs) {
            LocalDate dateFin = calculerDateFin(contrat);
            if (dateFin.isBefore(today)) {
                contrat.setStatut("terminé");
                contratRepository.save(contrat);
                log.info("Contrat ID {} mis à jour comme terminé.", contrat.getId());
            }
        }
    }

    private LocalDate calculerDateFin(Contrat contrat) {
        switch (contrat.getDureeContrat()) {
            case "1_mois":
                return contrat.getDebutContrat().plusMonths(1);
            case "3_mois":
                return contrat.getDebutContrat().plusMonths(3);
            case "6_mois":
                return contrat.getDebutContrat().plusMonths(6);
            case "1_an":
                return contrat.getDebutContrat().plusYears(1);
            case "1_voyage":
                return contrat.getDateRetour();
            default:
                throw new IllegalArgumentException("Durée de contrat invalide.");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Exécute tous les jours à minuit
    //@Scheduled(cron = "0 * * * * ?") // Exécute toutes les minutes
    public void checkAndUpdateContracts() {
        updateExpiredContracts();
    }

}
