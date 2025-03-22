package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;

import java.util.List;
import java.util.Optional;

public interface PartenaireServiceItf {
    List<Partenaire> allPartenaires();
    Optional<Partenaire> getPartenaireById(Long id);
}
