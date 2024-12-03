package com.gestioncontrats.gestioncontrats.model;

import org.springframework.data.repository.CrudRepository;

public interface OffreRepository extends CrudRepository<Offre, Long> {
    Offre findByNomOffre(String planName);
    boolean existsByNomOffre(String nomOffre);
}
