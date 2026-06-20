package com.theBridge.demoFormationJuin.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostEntity> posts;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "likeId", referencedColumnName = "id")
    private LikeEntity likes;
}