package com.gestionUtilisateurs.gestionUtilisateurs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "gestion_utilisateurs")
@NoArgsConstructor
@Getter
@Setter
public class Client extends Utilisateur{

    @Column(name = "maladie_chronique")
    private Boolean maladieChronique;

    @Size(max = 500, message = "La description de la maladie ne doit pas dépasser 500 caractères")
    @Column(name = "description_maladie", length = 500)
    private String descriptionMaladie;

}
