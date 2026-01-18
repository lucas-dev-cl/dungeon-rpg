package com.lucasdev.dungeon_rpg.Config;

import com.lucasdev.dungeon_rpg.Exception.UsernameAlreadyExistsException;
import com.lucasdev.dungeon_rpg.User.Entity.RoleEntity;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.RoleRepository;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Profile("dev")
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "admin")
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private String password;

    @Override
    public void run(String... args) throws Exception {
        createAdminInitializer("admin1");
    }

    public void createAdminInitializer(String username){
        if(userRepository.existsByUsername(username)){
            return; // no usar excepciones. Bootstrap ≠ validación de negocio, rompe el arranque de la app
        }

        RoleEntity roleEntity = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("El rol no existe"));

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRoleUser(List.of(roleEntity));

        userRepository.save(admin);
    }
}
