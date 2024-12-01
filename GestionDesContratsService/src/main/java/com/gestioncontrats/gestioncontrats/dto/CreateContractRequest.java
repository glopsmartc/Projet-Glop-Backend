package com.gestioncontrats.gestioncontrats.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateContractRequest {

    private String dureeContrat;
    private boolean assurerTransport;
    private boolean assurerPersonnes;
    private boolean voiture;
    private boolean trotinette;
    private boolean bicyclette;
    private Integer nombrePersonnes;
    private LocalDate debutContrat;
    private String destination;
    private LocalDate dateAller;
    private LocalDate dateRetour;
    private String numeroTelephone;
    private LocalDate dateNaissanceSouscripteur;
    private List<AccompagnantDto> accompagnants;
    private String planName;
    private String price;
}


