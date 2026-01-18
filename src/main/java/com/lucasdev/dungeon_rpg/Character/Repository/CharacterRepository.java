package com.lucasdev.dungeon_rpg.Character.Repository;

import com.lucasdev.dungeon_rpg.Character.Entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    List<Character> findByUserId(Long id);
    Optional<Character> findByIdAndUserId(Long id, Long userId);

}
