package com.lucasdev.dungeon_rpg.User.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "El campo no puede estar vacio")
    @Size(min = 6, message = "El username debe de tener por lo menos 6 caracteres")
    private String username;

    @NotBlank(message = "El campo no puede estar vacio")
    @Size(min = 6, message = "La contraseña debe de tener por lo menos 6 caracteres")
    private String password;

    public UserDTO(String username){
        this.username = username;
    }

}
