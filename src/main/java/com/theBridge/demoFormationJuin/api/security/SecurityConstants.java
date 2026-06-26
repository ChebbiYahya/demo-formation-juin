package com.theBridge.demoFormationJuin.api.security;

import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityConstants {
    // Algorithme utilise pour signer les JWT.
    public static final SignatureAlgorithm JWT_ALGORITHM = SignatureAlgorithm.HS512;

    // Duree de validite du token en millisecondes.
    public static final long JWT_EXPIRATION = 7000000;
}
