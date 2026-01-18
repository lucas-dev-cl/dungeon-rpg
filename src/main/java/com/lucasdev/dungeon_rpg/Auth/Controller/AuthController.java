package com.lucasdev.dungeon_rpg.Auth.Controller;

import com.lucasdev.dungeon_rpg.Auth.DTO.Request.LoginRequestDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Response.LoginResponseDTO;
import com.lucasdev.dungeon_rpg.Auth.DTO.Response.MessageResponse;
import com.lucasdev.dungeon_rpg.Auth.DTO.Request.RegisterRequestDTO;
import com.lucasdev.dungeon_rpg.Auth.Service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO){
        authService.register(registerRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Usuario creado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
}
