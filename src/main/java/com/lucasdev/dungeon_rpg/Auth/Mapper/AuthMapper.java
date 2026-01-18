package com.lucasdev.dungeon_rpg.Auth.Mapper;

import com.lucasdev.dungeon_rpg.Auth.DTO.Request.RegisterRequestDTO;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User registerDTOtoEntity(RegisterRequestDTO registerRequestDTO){
        return new User(
                registerRequestDTO.getUsername(),
                registerRequestDTO.getPassword()
        );
    }

}
