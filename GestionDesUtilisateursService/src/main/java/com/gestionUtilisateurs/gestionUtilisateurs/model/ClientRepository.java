package com.gestionUtilisateurs.gestionUtilisateurs.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
