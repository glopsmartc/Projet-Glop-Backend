package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContratServiceItf {
    Offre findMatchingOffre(CreateContractRequest request);

    Contrat createContract(CreateContractRequest request, MultipartFile pdfFile, String token) throws IOException;

    OffreResponse buildOffreResponse(Offre offreCorrespondante, CreateContractRequest request);

    List<Contrat> getContratsByClientEmail(String email);
}
