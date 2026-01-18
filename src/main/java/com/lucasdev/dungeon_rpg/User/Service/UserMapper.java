package com.lucasdev.dungeon_rpg.User.Service;

import com.lucasdev.dungeon_rpg.User.DTO.Request.UserDTO;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Funcion para transformar un UserDTO a User
    public User userDtoToUser(UserDTO userDTO){
        return new User(
                userDTO.getUsername(),
                userDTO.getPassword()
        );
    }

    // Funcion para transformar un User a UserDTO
    public UserDTO userToUserDTO(User user){
        return new UserDTO(
                user.getUsername()
        );
    }
}
