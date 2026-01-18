package com.lucasdev.dungeon_rpg.User.Repository;

import com.lucasdev.dungeon_rpg.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Metodos para autenticacion
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

}
