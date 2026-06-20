package com.theBridge.demoFormationJuin.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theBridge.demoFormationJuin.domain.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName roleName;

    @JsonIgnore
    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<UserEntity> users = new ArrayList<>();

    @Override
    public int hashCode() {
        return roleName != null ? roleName.hashCode() : 0;
    }
}
