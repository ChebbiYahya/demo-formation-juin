package com.theBridge.demoFormationJuin.api.controller;


import com.theBridge.demoFormationJuin.api.dto.AuthResponseDTO;
import com.theBridge.demoFormationJuin.api.dto.LoginDTO;
import com.theBridge.demoFormationJuin.api.dto.UserWithRoleRequest;
import com.theBridge.demoFormationJuin.api.security.JWTGenerator;
import com.theBridge.demoFormationJuin.domain.entities.RoleEntity;
import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import com.theBridge.demoFormationJuin.domain.enums.RoleName;
import com.theBridge.demoFormationJuin.repository.RoleRepository;
import com.theBridge.demoFormationJuin.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    // Service Spring Security qui verifie username/password.
    private final AuthenticationManager authenticationManager;

    // Repositories utilises pour lire et enregistrer users/roles.
    private final UserRepository userRepository;
    private final RoleRepository roleRepo;

    // Services utilises pour encoder les mots de passe et generer le JWT.
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator,
                          RoleRepository roleRepository) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.roleRepo = roleRepository;
    }

    @PostConstruct
    public void createDefaultAdminAccount() {

        // Creer les roles de base au demarrage.
        getOrCreateRole(RoleName.ADMIN);
        getOrCreateRole(RoleName.USER);

        // Au demarrage, creer un compte admin seulement s'il n'existe pas deja.
        if (!userRepository.existsByUsername("admin")) {

            UserEntity adminUser = new UserEntity();

            // Informations par defaut du compte admin.
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setEmail("admin@demo.com");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Demo");
            adminUser.setAddress("Tunis");

            // Recuperer le role ADMIN.
            RoleEntity adminRole = getOrCreateRole(RoleName.ADMIN);

            adminUser.setRole(adminRole);

            userRepository.save(adminUser);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserWithRoleRequest request) {

        try {
            UserEntity user = request.getUser();
            RoleName roleName = request.getRoleName() != null ? request.getRoleName() : RoleName.USER;

            if (userRepository.existsByUsername(user.getUsername())) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }

            // Encoder le mot de passe avant de sauvegarder l'utilisateur.
            String rawPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(rawPassword));

            // Associer le role demande au nouvel utilisateur.
            RoleEntity role = getOrCreateRole(roleName);
            user.setRole(role);

            userRepository.save(user);

            // Connecter directement l'utilisateur apres la creation du compte.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            rawPassword
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO(token, user);

            return new ResponseEntity<>(authResponseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Signup failed",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        try {

            // Verifier les identifiants envoyes dans le body de la requete.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // Enregistrer l'utilisateur authentifie dans le contexte Spring Security.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generer un JWT pour que le client l'utilise dans les prochaines requetes.
            String token = jwtGenerator.generateToken(authentication);

            // Recuperer l'utilisateur complet pour le retourner avec le token.
            UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                    .orElse(null);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO(token, user);

            return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);

        } catch (Exception e) {

            // Si l'authentification echoue, retourner une reponse 401.
            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    private RoleEntity getOrCreateRole(RoleName roleName) {
        return roleRepo.findByRoleName(roleName)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setRoleName(roleName);
                    return roleRepo.save(newRole);
                });
    }
}
