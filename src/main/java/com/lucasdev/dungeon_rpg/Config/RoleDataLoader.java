package com.lucasdev.dungeon_rpg.Config;

import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        // Creamos los roles
        createRoleIfNotExists("PLAYER");
        createRoleIfNotExists("ADMIN");

    }

    // Metodo que crea un rol si no existe y lo guarda en la BD
    public void createRoleIfNotExists(String name){
        String roleName = "ROLE_" + name;
        roleRepository
                .findByName(roleName)
                .orElseGet(() -> roleRepository.save(new RoleEntity(roleName)));
    }
}
