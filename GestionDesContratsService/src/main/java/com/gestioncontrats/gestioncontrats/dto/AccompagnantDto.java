package com.gestioncontrats.gestioncontrats.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccompagnantDto {
    private String nom;
    private String prenom;
    private String sexe;
    private LocalDate dateNaissance;
}
