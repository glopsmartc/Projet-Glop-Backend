package com.gestionUtilisateurs.gestionUtilisateurs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    Partenaire findByEmail(String email);
}
