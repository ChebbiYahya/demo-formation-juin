package com.theBridge.demoFormationJuin.api.dto;

import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserEntity user;

    public AuthResponseDTO(String accessToken, UserEntity user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
