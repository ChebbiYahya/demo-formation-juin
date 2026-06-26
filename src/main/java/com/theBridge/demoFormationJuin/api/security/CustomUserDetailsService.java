package com.theBridge.demoFormationJuin.api.security;


import com.theBridge.demoFormationJuin.domain.entities.RoleEntity;
import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import com.theBridge.demoFormationJuin.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    // Repository utilise pour chercher les utilisateurs dans la base de donnees.
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Chercher l'utilisateur par username, sinon lancer une erreur Spring Security.
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convertir notre UserEntity en UserDetails compris par Spring Security.
        return new User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(RoleEntity userRole) {

        // Transformer le role de l'application en autorite Spring Security.
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority(userRole.getRoleName().toString())
        );
        return authorities;
    }
}
