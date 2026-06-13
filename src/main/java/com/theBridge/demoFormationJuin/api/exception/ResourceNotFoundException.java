package com.theBridge.demoFormationJuin.api.exception;

/*
 * Cette exception represente le cas "ressource non trouvee".
 *
 * Dans notre projet, une ressource peut etre un User.
 * Exemple:
 * - GET /users/10
 * - Si aucun utilisateur avec id = 10 n'existe dans la base
 * - Le service lance cette exception
 *
 * Elle est ensuite interceptee par GlobalExceptionHandler pour retourner
 * une reponse HTTP 404 NOT FOUND au lieu d'une erreur serveur 500.
 */
public class ResourceNotFoundException extends RuntimeException {

    /*
     * Le constructeur recoit un message explicatif.
     *
     * Exemple d'utilisation:
     * throw new ResourceNotFoundException("Utilisateur introuvable avec id: " + id);
     *
     * super(message) envoie ce message a la classe mere RuntimeException.
     * Ensuite, on peut recuperer ce message avec exception.getMessage()
     * dans GlobalExceptionHandler.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
