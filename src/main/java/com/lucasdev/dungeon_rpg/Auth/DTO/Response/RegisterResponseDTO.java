package com.lucasdev.dungeon_rpg.Auth.DTO.Response;


// Existe una forma mas moderna y eficiente

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterResponseDTO {

    String username;
    String message;

}
