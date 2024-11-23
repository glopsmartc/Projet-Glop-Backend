package com.gestioncontrats.gestioncontrats.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "accompagnant", schema = "gestion_contrats")
@NoArgsConstructor
@Getter
@Setter
public class Accompagnant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String sexe;
    private LocalDate dateNaissance;
}

