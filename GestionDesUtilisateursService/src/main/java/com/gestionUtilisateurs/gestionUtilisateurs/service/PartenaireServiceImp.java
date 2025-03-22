package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;
import com.gestionUtilisateurs.gestionUtilisateurs.model.PartenaireRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartenaireServiceImp implements PartenaireServiceItf {
    private final PartenaireRepository partenaireRepository;

    public PartenaireServiceImp(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    @Override
    public List<Partenaire> allPartenaires() {
        List<Partenaire> partenaires = new ArrayList<>();

        partenaireRepository.findAll().forEach(partenaires::add);

        return partenaires;
    }
    
    @Override
    public Optional<Partenaire> getPartenaireById(Long id) {
        return partenaireRepository.findById(id);
    } 
}
