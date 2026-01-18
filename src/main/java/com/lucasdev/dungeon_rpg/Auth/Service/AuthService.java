package com.lucasdev.dungeon_rpg.Auth.Service;

import com.lucasdev.dungeon_rpg.Auth.DTO.Request.LoginRequestDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Request.RegisterRequestDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Response.LoginResponseDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Response.RegisterResponseDTO;
import com.lucasdev.dungeon_rpg.Auth.Mapper.AuthMapper;
import com.lucasdev.dungeon_rpg.Exception.UsernameAlreadyExistsException;
import com.lucasdev.dungeon_rpg.Security.Jwt.JwtService;
import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Metodo para crear un usuario
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO){
        if(userRepository.existsByUsername(registerRequestDTO.getUsername())){
            throw new UsernameAlreadyExistsException("El usuario " + registerRequestDTO.getUsername() + " ya existe");
        }

        User user = authMapper.registerDTOtoEntity(registerRequestDTO);

        RoleEntity role = roleRepository.findByName("ROLE_PLAYER")
                .orElseThrow(() -> new IllegalStateException("Role no encontrado"));

        user.setRoleUser(List.of(role));

        // Hasheamos la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new RegisterResponseDTO(savedUser.getUsername(), "El usuario " + user.getUsername() + " ha sido registrado exitosamente.");
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){

        // Autenticamos con spring security
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.getUsername(),
                                loginRequestDTO.getPassword()
                        )
                );


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new LoginResponseDTO(token, userDetails.getUsername(), roles);
    }
}
