package com.gestionUtilisateurs.gestionUtilisateurs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(schema = "gestion_utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor //constructeur par defaut without arguments
@Getter
@Setter
public class Utilisateur{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for PostgreSQL
    @Column(name = "id_user")
    private Long idUser;

    @NotBlank(message = "Le nom est obligatoire") // Must not be null or blank
    @Size(max = 35, message = "Le nom ne doit pas dépasser 35 caractères")
    @Column(name = "nom", nullable = false, length = 35)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire") // Must not be null or blank
    @Size(max = 35, message = "Le prénom ne doit pas dépasser 35 caractères")
    @Column(name = "prenom", nullable = false, length = 35)
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    @Column(name = "adresse", nullable = false, length = 255)
    private String adresse;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Column(name = "email", nullable = false, unique = true) // Email must be unique
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Le numéro de téléphone est invalide")
    @Column(name = "num_tel", nullable = false, unique = true)
    private String numTel;

}
