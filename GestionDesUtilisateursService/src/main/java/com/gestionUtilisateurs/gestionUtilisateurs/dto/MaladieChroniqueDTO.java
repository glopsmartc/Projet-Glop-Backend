package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MaladieChroniqueDTO {
    private Boolean maladieChronique;
    private String descriptionMaladie;
    private String email;
}