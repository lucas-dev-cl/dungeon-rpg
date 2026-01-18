package com.lucasdev.dungeon_rpg.ServiceTest;

import com.lucasdev.dungeon_rpg.Exception.UserNotFoundException;
import com.lucasdev.dungeon_rpg.Exception.UsernameAlreadyExistsException;
import com.lucasdev.dungeon_rpg.User.DTO.Request.UserDTO;
import com.lucasdev.dungeon_rpg.User.DTO.Response.UserResponse;
import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import com.lucasdev.dungeon_rpg.User.Service.UserMapper;
import com.lucasdev.dungeon_rpg.User.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Simulamos el repositorio y el mapper
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Happy path: se crea el usuario correctamente
    @Test
    void createAdminTest(){

        UserDTO userDTO = new UserDTO("lucas123", "1234");
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        RoleEntity role = new RoleEntity("ROLE_PLAYER");

        // Stubeamos userRepository primer condicional
        when(userRepository.existsByUsername(userDTO.getUsername()))
                .thenReturn(false);

        when(userMapper.userDtoToUser(userDTO))
                .thenReturn(user);

        when(passwordEncoder.encode("1234"))
                .thenReturn("hashed");

        when(roleRepository.findByName("ROLE_ADMIN"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponse userResponse =
                userService.createAdmin(userDTO);

        assertEquals("lucas123", userResponse.getUsername());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("1234");
    }

    // Error path: el nombre de usuario ya existe
    @Test
    void createAdminTest_UsernameAlreadyExistsException(){
        UserDTO userDTO = new UserDTO("lucas123", "1234");

        // Stubeamos el repositorio al buscar username
        when(userRepository.existsByUsername(userDTO.getUsername()))
                .thenReturn(true);

        // Comprobamos la excepcion
        UsernameAlreadyExistsException ex = assertThrows(
                UsernameAlreadyExistsException.class, // Excepcion que esperamos que se lance
                () -> userService.createAdmin(userDTO)
        );

        assertTrue(ex.getMessage().contains("lucas123")); // Verificamos que el error del mensaje contenga el nombre de usuario

        verify(userRepository, never()).save(any(User.class));
    }

    // Error path: el rol del usuario no es encontrado
    @Test
    void createAdminTest_IllegalStateException(){
        UserDTO userDTO = new UserDTO("lucas123", "1234");
        User user = new User(userDTO.getUsername(), userDTO.getPassword());

        when(userRepository.existsByUsername(userDTO.getUsername()))
                .thenReturn(false);

        when(userMapper.userDtoToUser(userDTO))
                .thenReturn(user);

        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.empty()); // Si esta vacio entonce lanza la excepcion

        assertThrows(
                IllegalStateException.class,
                () -> userService.createAdmin(userDTO)
        );
    }

    // Happy path: El usuario es eliminado y el id existe
    @Test
    void deleteUserTest(){
        User user = new User(
                "Lucas",
                "812"
        );

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        userService.deleteUser(112L);

        verify(userRepository).delete(user);
    }

    // Error path: El usuario no es encontrado
    @Test
    void deleteUser_throwUserNotFoundException(){
        Long id = 12l;

        // Cuando usemos el "findById" lanza el error del usuario no encontrado
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Esperamos que lance la excepcion
        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(id)
        );

        verify(userRepository, never()).delete(any());
    }
}