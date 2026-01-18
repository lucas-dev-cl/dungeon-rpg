package com.lucasdev.dungeon_rpg.DungeonRun.Repository;

import com.lucasdev.dungeon_rpg.DungeonRun.Entity.DungeonRun;
import com.lucasdev.dungeon_rpg.DungeonRun.Entity.StatusRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DungeonRunRepository extends JpaRepository<DungeonRun, Long> {

    List<DungeonRun> findByCharacterId(Long characterId);
    List<DungeonRun> findByCharacterIdAndStatusRun(Long characterId, StatusRun status);

    // Evitamos que un personaje tenga 2 runs activas
    boolean existsByCharacterIdAndStatusRun(Long characterId, StatusRun status);

}
