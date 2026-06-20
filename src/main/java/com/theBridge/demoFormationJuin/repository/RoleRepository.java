package com.theBridge.demoFormationJuin.repository;

import com.theBridge.demoFormationJuin.domain.entities.RoleEntity;
import com.theBridge.demoFormationJuin.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
