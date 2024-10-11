package com.gestionUtilisateurs.gestionUtilisateurs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {

        List<Utilisateur> mockUsers = new ArrayList<>();
        mockUsers.add(new Utilisateur("John", "Doe@gmail.com"));
        mockUsers.add(new Utilisateur( "Jane", "DoeJane@gmail.com"));

        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<Utilisateur> users = userController.getAllUsers();

        //Assert
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("Jane", users.get(1).getName());
    }
}
