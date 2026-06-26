package com.theBridge.demoFormationJuin.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Point d'entree utilise quand une requete non authentifiee est refusee.
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTAuthentificationFilter jwtAuthenticationFilter
    ) throws Exception {
        // Configuration principale de Spring Security pour l'API.
        http.cors(Customizer.withDefaults())
                // CSRF est desactive car l'API utilise des JWT et pas des sessions web.
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthEntryPoint))
                // Mode stateless: le serveur ne garde pas de session utilisateur.
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Les routes d'authentification restent publiques.
                        .requestMatchers("/auth/**").permitAll()
                        // Seul ADMIN peut gerer les roles.
                        .requestMatchers("/roles/**").hasAuthority("ADMIN")
                        // Exemple de routes accessibles seulement par ADMIN.
                        .requestMatchers("/test/admin/**").hasAuthority("ADMIN")
                        // Exemple de routes accessibles seulement par USER.
                        .requestMatchers("/test/user/**").hasAuthority("USER")
                        // Toutes les autres routes demandent un utilisateur connecte.
                        .anyRequest().authenticated()
                );

        // Le filtre JWT est execute avant le filtre classique username/password.
        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider authenticationProvider
    ) {
        // AuthenticationManager utilise le provider qui sait charger les users.
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        // Compare le mot de passe saisi avec le mot de passe encode en base.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt encode les mots de passe avant de les enregistrer.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthentificationFilter jwtAuthenticationFilter(
            JWTGenerator jwtGenerator,
            CustomUserDetailsService customUserDetailsService
    ) {
        // Creation du filtre qui lit et valide le JWT dans chaque requete.
        return new JWTAuthentificationFilter(jwtGenerator, customUserDetailsService);
    }
}
