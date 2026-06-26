package com.theBridge.demoFormationJuin.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthentificationFilter extends OncePerRequestFilter {
    // Service qui genere, lit et valide les tokens JWT.
    private final JWTGenerator jwtGenerator;

    // Service qui charge l'utilisateur depuis la base de donnees.
    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthentificationFilter(
            JWTGenerator jwtGenerator,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtGenerator = jwtGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extraire le JWT depuis l'en-tete Authorization de la requete.
        String token = getJWTFromRequest(request);

        // Verifier que le token existe, qu'il est valide et qu'aucun user n'est deja connecte.
        if (StringUtils.hasText(token) && jwtGenerator.validationToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Recuperer le username stocke dans le token.
            String username = jwtGenerator.getUsernameFromJwt(token);

            // Charger les details de l'utilisateur depuis la base de donnees.
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Creer une authentification Spring avec les roles de l'utilisateur.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // Placer l'utilisateur dans le contexte de securite pour cette requete.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continuer vers les autres filtres puis vers le controller.
        filterChain.doFilter(request, response);
    }

    // Methode pour extraire le JWT depuis l'en-tete "Authorization".
    private String getJWTFromRequest(HttpServletRequest request) {

        // Recuperer l'en-tete Authorization de la requete.
        String bearerToken = request.getHeader("Authorization");

        // Si le format est "Bearer token", on retourne seulement le token.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
