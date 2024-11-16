package com.gestioncontrats.gestioncontrats.service;

import com.gestioncontrats.gestioncontrats.dto.CreateContractRequest;
import com.gestioncontrats.gestioncontrats.dto.OffreResponse;
import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.Offre;

public interface ContratServiceItf {
    Contrat createContract(CreateContractRequest request);

    Offre findMatchingOffre(CreateContractRequest request);

    OffreResponse buildOffreResponse(Offre offreCorrespondante, CreateContractRequest request);
}
