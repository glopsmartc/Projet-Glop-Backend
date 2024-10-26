package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;

import java.util.List;

public interface UserServiceItf {
    List<Utilisateur> allUsers();

    Utilisateur createConseiller(RegisterUserDto input);
}
