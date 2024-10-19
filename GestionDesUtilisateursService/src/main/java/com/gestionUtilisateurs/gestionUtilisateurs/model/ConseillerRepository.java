package com.gestionUtilisateurs.gestionUtilisateurs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConseillerRepository extends JpaRepository <Conseiller, Long> {
    Conseiller findByEmail(String email);
}
