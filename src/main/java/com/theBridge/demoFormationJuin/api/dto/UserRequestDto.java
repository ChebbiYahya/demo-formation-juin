package com.theBridge.demoFormationJuin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @Size(min = 3, max = 10, message = "Le nom doit contenir entre 3 et 10 caracteres")
    private String firstName;

    private String lastName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email est invalide")
    private String email;

    private String password;

    private String address;

    @NotBlank(message = "Le username est obligatoire")
    private String username;

    private String confirmPassword;
}
