package com.gestionUtilisateurs.gestionUtilisateurs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "gestion_utilisateurs")
@NoArgsConstructor
@Getter
@Setter
public class Partenaire extends Utilisateur{
    @Column(name = "nom_entreprise")
    private String nomEntreprise;

    @Column(name = "zone_geographique")
    private String zoneGeographique;
}
