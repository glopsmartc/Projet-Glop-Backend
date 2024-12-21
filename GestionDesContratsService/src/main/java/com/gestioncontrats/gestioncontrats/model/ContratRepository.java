package com.gestioncontrats.gestioncontrats.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContratRepository extends CrudRepository<Contrat, Long> {
    List<Contrat> findByClient(String client);
}
