package com.gestionUtilisateurs.gestionUtilisateurs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    Medecin findByEmail(String email);
}
