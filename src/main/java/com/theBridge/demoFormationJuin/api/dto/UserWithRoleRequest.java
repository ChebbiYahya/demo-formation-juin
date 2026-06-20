package com.theBridge.demoFormationJuin.api.dto;

import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import com.theBridge.demoFormationJuin.domain.enums.RoleName;

public class UserWithRoleRequest {
    private UserEntity user;
    private RoleName roleName;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
