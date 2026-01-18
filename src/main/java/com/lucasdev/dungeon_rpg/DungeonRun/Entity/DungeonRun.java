package com.lucasdev.dungeon_rpg.DungeonRun.Entity;

import com.lucasdev.dungeon_rpg.Character.Entity.CharacterEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dungeon")
public class DungeonRun {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusRun statusRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "character_id"
    )
    private CharacterEntity character;
}
