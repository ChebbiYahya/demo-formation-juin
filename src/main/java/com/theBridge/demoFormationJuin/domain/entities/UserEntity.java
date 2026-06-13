package com.theBridge.demoFormationJuin.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    @Size(max = 10, min = 3,
            message = "Le nom ne doit pas dépasser 10 caractères")
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true, length = 40)
    private String email;

    private String password;

    private String address;

    @Column(nullable = false, unique = true)
    private String username;

    private String confirmPassword;

}
