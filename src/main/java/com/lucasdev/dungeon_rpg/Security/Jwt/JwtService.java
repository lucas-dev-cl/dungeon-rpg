package com.lucasdev.dungeon_rpg.Security.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder() // creamos el token
                .setSubject(userDetails.getUsername()) // Establecemos el nombre del dueño del jwt
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .toList())
                .setIssuedAt(new Date()) // Cuando se emitio
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration())) // Expiracion en milisegundos
                .signWith(getKey()) // La firmamos
                .compact(); // Cerramos y creamos
    }

    // criptografiamos la llave
    public Key getKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)); // aca no se que onda xd creo que hasheamos
    }

    // Extraemos todas las claims, gracias a este nosotros despues podemos acceder al contenido del jwt, modularizandolo con diferentes funciones
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder() // Leemos y validamos token
                .setSigningKey(getKey()) // Verificamos que la clave sea la misma
                .build()// Ahora si creamos el token
                .parseClaimsJws(token) // Valida todo
                .getBody(); // Devolvemos el payload
    }

    // Metodo para extraer el username del token
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    // Metodo para extraer la expiracion del token
    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    // Metodo para comprobar si el token esta expirado
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Metodo para comprobar si el token es valido
    // Comprobando si el nombre de usuario coincide con el extraido de la bd con userdetails
    // obviamente el subject deberia de ser unico
    // y comprobando que no este expirado
    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
