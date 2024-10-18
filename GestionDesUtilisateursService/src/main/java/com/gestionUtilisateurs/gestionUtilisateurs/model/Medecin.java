package com.gestionUtilisateurs.gestionUtilisateurs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(schema = "gestion_utilisateurs")
@NoArgsConstructor
@Getter
@Setter
public class Medecin extends Utilisateur{

    @Column(name = "specialite")
    private String specialite;

    @Column(name = "disponibilites", columnDefinition = "text[]") // tableau de texte
    private String[] disponibilites; // Tableau pour stocker les horaires de disponibilité

    @Column(name = "deplacement")
    private boolean deplacement;
}
