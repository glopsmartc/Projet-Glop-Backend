package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegisterUserDto {
    private String email;

    private String password;

    private String nom;

    private String prenom;

    private String numTel;

    private String adresse;

    private String dateNaissanceString; // Chaîne pour la date de naissance

    private LocalDate dateNaissance;


}
