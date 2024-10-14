package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserDto {
    private String email;

    private String password;
}
