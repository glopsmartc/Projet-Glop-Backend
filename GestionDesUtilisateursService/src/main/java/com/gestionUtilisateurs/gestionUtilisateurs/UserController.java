package com.gestionUtilisateurs.gestionUtilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //recuperer la liste des etudiants
    @GetMapping("/users")
    public List<Utilisateur> getAllUsers() {
        return userService.getAllUsers();
    }
}
