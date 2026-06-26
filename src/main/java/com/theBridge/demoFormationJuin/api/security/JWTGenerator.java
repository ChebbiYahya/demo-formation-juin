package com.theBridge.demoFormationJuin.api.security;

import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import com.theBridge.demoFormationJuin.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTGenerator {
    // Cle generee avec l'algorithme HS512.
    private static final Key key = Keys.secretKeyFor(SecurityConstants.JWT_ALGORITHM);
    private final UserRepository userRepository;

    public JWTGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {

        // Nom de l'utilisateur connecte.
        String username = authentication.getName();

        // Dates de creation et d'expiration du token.
        Date currentDate = new Date();
        Date expirationDate = new Date(
                currentDate.getTime() + SecurityConstants.JWT_EXPIRATION
        );

        // Recuperer les roles/permissions donnes par Spring Security.
        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        // Transformer les roles en liste de textes pour les mettre dans le JWT.
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Recuperer l'utilisateur complet pour ajouter ses informations au token.
        UserEntity user = userRepository.findByUsername(username).get();

        // Construire le JWT avec le username, les infos user, les roles et la signature.
        String token = Jwts.builder()
                .setSubject(username)
                .claim("user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail()
                ))
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key, SecurityConstants.JWT_ALGORITHM)
                .compact();

        System.out.println("token : " + token);

        return token;
    }

    public String getUsernameFromJwt(String token) {

        // Lire le contenu du JWT avec la meme cle utilisee pour le signer.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validationToken(String token) {
        try {
            // Si le parsing reussit, le token est valide et non modifie.
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            // En cas de token expire ou invalide, Spring Security refusera la requete.
            throw new AuthenticationCredentialsNotFoundException(
                    "JWT was expired or incorrect",
                    e.fillInStackTrace()
            );
        }
    }
}
