package com.theBridge.demoFormationJuin.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
 * Cette classe centralise la gestion des erreurs de l'API.
 *
 * Sans cette classe, chaque methode du controller devrait faire des try/catch
 * pour transformer les exceptions Java en reponses HTTP propres.
 *
 * Avec @RestControllerAdvice, Spring intercepte automatiquement les exceptions
 * lancees depuis les controllers ou services, puis appelle la bonne methode
 * annotee avec @ExceptionHandler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Cette methode gere les erreurs de type ResourceNotFoundException.
     *
     * Exemple concret dans le CRUD User:
     * - Le service cherche un user avec userRepository.findById(id)
     * - Si aucun user n'existe avec cet id, on lance ResourceNotFoundException
     * - Spring arrive ici automatiquement
     * - On retourne une reponse HTTP 404 NOT FOUND avec un message JSON
     *
     * Exemple de reponse:
     * {
     *   "message": "Utilisateur introuvable avec id: 5"
     * }
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException exception) {

        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /*
     * Cette methode gere les erreurs de validation.
     *
     * Elle est appelee quand un DTO annote avec @Valid n'est pas valide.
     * Exemple dans UserController:
     * public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto)
     *
     * Si firstName ne respecte pas @Size, ou si email ne respecte pas @Email,
     * Spring lance MethodArgumentNotValidException automatiquement.
     *
     * On retourne une reponse HTTP 400 BAD REQUEST, car le client a envoye
     * des donnees invalides.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        /*
         * getBindingResult() contient toutes les erreurs trouvees par Spring.
         * getFieldErrors() retourne uniquement les erreurs liees aux champs.
         *
         * Pour chaque erreur:
         * - error.getField() donne le nom du champ
         * - error.getDefaultMessage() donne le message defini dans le DTO
         */
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        /*
         * badRequest() correspond au status HTTP 400.
         * Le body contient toutes les erreurs de validation.
         *
         * Exemple de reponse:
         * {
         *   "email": "L'email est invalide",
         *   "username": "Le username est obligatoire"
         * }
         */
        return ResponseEntity.badRequest().body(errors);
    }
}
