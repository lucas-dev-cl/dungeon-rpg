package com.lucasdev.dungeon_rpg.User.Entity;

import com.lucasdev.dungeon_rpg.Character.Entity.CharacterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El que sea unico se verifica en la bd
    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), // dueña de la relacion
            inverseJoinColumns = @JoinColumn(name = "role_id") // entidad del otro lado
    )
    private List<RoleEntity> roleUser;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CharacterEntity> characters;

    public User(String username, String password, List<RoleEntity> roleUser){
        this.username = username;
        this.password = password;
        this.roleUser = roleUser;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
