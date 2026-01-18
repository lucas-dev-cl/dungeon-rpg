package com.lucasdev.dungeon_rpg.Security.User;

import com.lucasdev.dungeon_rpg.Exception.UserNotFoundException;
import com.lucasdev.dungeon_rpg.User.Entity.User;
import com.lucasdev.dungeon_rpg.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username", username));

        return UserDetailsImpl.build(user);
    }
    
}
