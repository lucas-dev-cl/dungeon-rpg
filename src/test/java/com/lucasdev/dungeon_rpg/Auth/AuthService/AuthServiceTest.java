package com.lucasdev.dungeon_rpg.Auth.AuthService;

import com.lucasdev.dungeon_rpg.Auth.DTO.Request.RegisterRequestDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Response.RegisterResponseDTO;
import com.lucasdev.dungeon_rpg.Auth.Mapper.AuthMapper;
import com.lucasdev.dungeon_rpg.Auth.Service.AuthService;
import com.lucasdev.dungeon_rpg.Exception.UsernameAlreadyExistsException;
import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthServiceTest {

    @Mock
    private AuthMapper authMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // Happy path: se crea el usuario correctamente
    @Test
    void registerTest(){

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("lucas123", "1234");
        User user = new User(registerRequestDTO.getUsername(), registerRequestDTO.getPassword());
        RoleEntity role = new RoleEntity("ROLE_PLAYER");

        // Stubeamos userRepository primer condicional
        when(userRepository.existsByUsername(registerRequestDTO.getUsername()))
                .thenReturn(false);

        when(authMapper.registerDTOtoEntity(registerRequestDTO))
                .thenReturn(user);

        when(passwordEncoder.encode("1234"))
                .thenReturn("hashed");

        when(roleRepository.findByName("ROLE_PLAYER"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        RegisterResponseDTO response =
                authService.register(registerRequestDTO);

        assertEquals("lucas123", response.getUsername());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("1234");
    }

    // Error path: el nombre de usuario ya existe
    @Test
    void registerTest_UsernameAlreadyExistsException(){
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("lucas123", "1234");

        // Stubeamos el repositorio al buscar username
        when(userRepository.existsByUsername(registerRequestDTO.getUsername()))
                .thenReturn(true);

        // Comprobamos la excepcion
        UsernameAlreadyExistsException ex = assertThrows(
                    UsernameAlreadyExistsException.class, // Excepcion que esperamos que se lance
                    () -> authService.register(registerRequestDTO)
            );

        assertTrue(ex.getMessage().contains("lucas123")); // Verificamos que el error del mensaje contenga el nombre de usuario

        verify(userRepository, never()).save(any(User.class));
    }

    // Error path: el rol del usuario no es encontrado
    @Test
    void registerTest_IllegalStateException(){
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("lucas123", "1234");
        User user = new User(registerRequestDTO.getUsername(), registerRequestDTO.getPassword());

        when(userRepository.existsByUsername(registerRequestDTO.getUsername()))
                .thenReturn(false);

        when(authMapper.registerDTOtoEntity(registerRequestDTO))
                .thenReturn(user);

        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.empty()); // Si esta vacio entonce lanza la excepcion

        assertThrows(
                IllegalStateException.class,
                () -> authService.register(registerRequestDTO)
        );
    }
}
