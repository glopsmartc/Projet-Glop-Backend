package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImp implements ClientServiceItf {
    private final ClientRepository clientRepository;

    public ClientServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> allClients() {
        List<Client> clients = new ArrayList<>();

        clientRepository.findAll().forEach(clients::add);

        return clients;
    }
}
