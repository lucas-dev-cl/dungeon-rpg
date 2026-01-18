package com.lucasdev.dungeon_rpg.Character.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CharacterDTO {

    private String name;
    private int level;
    private int hp;

}
