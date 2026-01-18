package com.lucasdev.dungeon_rpg.Exception;

public class UserNotFoundException extends RuntimeException {
    // Object acepta cualquier valor, y los convierte dependiendo del contexto
    public UserNotFoundException(String field, Object value) {
        super("No se ha encontrado usuario con el " + field + ": " + value);
    }
}
