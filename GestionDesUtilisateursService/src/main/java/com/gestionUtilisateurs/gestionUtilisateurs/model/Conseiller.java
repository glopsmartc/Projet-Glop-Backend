package com.gestionUtilisateurs.gestionUtilisateurs.model;

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
public class Conseiller extends Utilisateur{
}
