package com.lucasdev.dungeon_rpg.Character.Entity;

import com.lucasdev.dungeon_rpg.DungeonRun.Entity.DungeonRun;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "characters")
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int level;
    private int hp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "character",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DungeonRun> dungeonRuns;
}
