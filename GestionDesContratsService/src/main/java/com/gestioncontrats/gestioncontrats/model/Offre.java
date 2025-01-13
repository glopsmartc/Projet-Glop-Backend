package com.gestioncontrats.gestioncontrats.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "offre", schema = "gestion_contrats")
@NoArgsConstructor
@Getter
@Setter
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_offre", nullable = false)
    private String nomOffre;

    @Column(name = "description_min", columnDefinition = "TEXT")
    private String descriptionMin;

    @Column(name = "description_max", columnDefinition = "TEXT")
    private String descriptionMax;

    @Column(name = "prix_min")
    private String prixMin;

    @Column(name = "prix_max")
    private String prixMax;

    public Offre(String nomOffre, String descriptionMin, String descriptionMax, String prixMin, String prixMax) {
        this.nomOffre = nomOffre;
        this.descriptionMin = descriptionMin;
        this.descriptionMax = descriptionMax;
        this.prixMin = prixMin;
        this.prixMax = prixMax;
    }
}