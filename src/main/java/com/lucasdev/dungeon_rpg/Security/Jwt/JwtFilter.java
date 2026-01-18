package com.lucasdev.dungeon_rpg.Security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Leer el header de autorizacion
        String authHeader = request.getHeader("Authorization");

        // Si esta vacio o no empieza con bearer (que asi empiezan las autorizaciones con jwt)
        // Seguimos sin hacer nada, esto es mas que nada para endpoints que no necesitan autenticacion ni autorizacion, son publicos
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response); // No se que es esto, ya lo vamos a ver, se que es de los filtros del configuration
            return; // Seguimos sin autenticar
        }

        // Obtenemos el jwt, obtenemos saltando los primeros 7 elementos, o sea, salteamos el "Bearer " porque no es parte del token
        String jwt = authHeader.substring(7);

        String username = jwtService.extractUsername(jwt);

        // Supongo que la condicion verifica si el username no esta nulo, y si la autenticacion de este no ha sido contextualizada, o sea, no se ha hecho
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Cargamos al usuario, este lo obtuvimos extrayendo el subject del jwt
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(jwt, user)){
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Establecemos en el contexto la autenticacion, para que no vuelva a pedir las credenciales, ya en cada request revisa el token
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }
        // Terminamos con el filtrado, seguimos con los otros filtros de seguridad
        filterChain.doFilter(request, response);

    }
}
