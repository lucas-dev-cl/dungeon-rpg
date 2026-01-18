package com.lucasdev.dungeon_rpg.User.Service;

import com.lucasdev.dungeon_rpg.Exception.UserNotFoundException;
import com.lucasdev.dungeon_rpg.Exception.UsernameAlreadyExistsException;
import com.lucasdev.dungeon_rpg.User.DTO.Request.UserDTO;
import com.lucasdev.dungeon_rpg.User.DTO.Response.UserResponse;
import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createAdmin(UserDTO userDTO){
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new UsernameAlreadyExistsException("El usuario " + userDTO.getUsername() + " ya existe");
        }

        User user = userMapper.userDtoToUser(userDTO);

        RoleEntity role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("Role no encontrado"));

        user.setRoleUser(List.of(role));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getUsername(), "El usuario " + user.getUsername() + " ha sido registrado exitosamente.");
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id)); // Si no existe, o sea es empty, entonces lanzamos el error

        userRepository.delete(user);
    }

}
