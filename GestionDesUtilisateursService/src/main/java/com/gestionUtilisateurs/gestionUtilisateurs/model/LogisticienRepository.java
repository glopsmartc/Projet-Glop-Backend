package com.gestionUtilisateurs.gestionUtilisateurs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticienRepository extends JpaRepository<Logisticien, Long> {
    Logisticien findByEmail(String email);
}
