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

    @ElementCollection
    @CollectionTable(name = "medecin_disponibilites", schema = "gestion_utilisateurs", joinColumns = @JoinColumn(name = "medecin_id"))
    @Column(name = "disponibilite")
    private List<String> disponibilites;

    @Column(name = "deplacement")
    private boolean deplacement;
}
