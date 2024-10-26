package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginUserDto {
    private String email;

    private String password;
}
