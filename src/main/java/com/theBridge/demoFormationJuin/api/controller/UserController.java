package com.theBridge.demoFormationJuin.api.controller;


import com.theBridge.demoFormationJuin.api.dto.UserRequestDto;
import com.theBridge.demoFormationJuin.api.dto.UserResponseDto;
import com.theBridge.demoFormationJuin.application.interfaces.UserInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userInterface.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userInterface.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userInterface.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity.ok(userInterface.updateUser(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userInterface.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // NAMED METHOD APIs
    // Ces routes testent les methodes dont Spring genere la requete a partir du nom.
    @GetMapping("/named/exists-by-username/{username}")
    public ResponseEntity<Boolean> existsByUsernameNamed(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.existsByUsernameNamed(username));
    }

    @GetMapping("/named/by-username/{username}")
    public ResponseEntity<UserResponseDto> findByUsernameNamed(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.findByUsernameNamed(username));
    }

    @GetMapping("/named/by-email-address")
    public ResponseEntity<List<UserResponseDto>> findByEmailAndAddressStartingWithNamed(
            @RequestParam String email,
            @RequestParam String address
    ) {
        return ResponseEntity.ok(userInterface.findByEmailAndAddressStartingWithNamed(email, address));
    }

    @GetMapping("/named/by-email")
    public ResponseEntity<UserResponseDto> findByEmailNamed(@RequestParam String email) {
        return ResponseEntity.ok(userInterface.findByEmailNamed(email));
    }

    // JPQL METHOD APIs
    // Ces routes testent les requetes @Query qui utilisent les noms Java: UserEntity, username, etc.
    @GetMapping("/jpql/by-username/{username}")
    public ResponseEntity<UserResponseDto> findUserByUsernameJPQL(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.findUserByUsernameJPQL(username));
    }

    @GetMapping("/jpql/exists-by-username/{username}")
    public ResponseEntity<Boolean> existsByUsernameJPQL(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.existsByUsernameJPQL(username));
    }

    // SQL METHOD APIs
    // Ces routes testent les requetes SQL natives qui utilisent la table users.
    @GetMapping("/sql/by-username/{username}")
    public ResponseEntity<UserResponseDto> findByUsernameSQL(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.findByUsernameSQL(username));
    }

    @GetMapping("/sql/exists-by-username/{username}")
    public ResponseEntity<Boolean> existsByUsernameSQL(@PathVariable String username) {
        return ResponseEntity.ok(userInterface.existsByUsernameSQL(username));
    }

    @GetMapping("/sql/by-cle")
    public ResponseEntity<List<UserResponseDto>> findByCleSQL(@RequestParam String cle) {
        return ResponseEntity.ok(userInterface.findByCleSQL(cle));
    }

    @GetMapping("/sql/by-domain")
    public ResponseEntity<List<UserResponseDto>> findByDomainSQL(@RequestParam String domain) {
        return ResponseEntity.ok(userInterface.findByDomainSQL(domain));
    }

}
