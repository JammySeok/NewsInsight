package com.example.NewsInsight.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String userid;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    private String provider;
    private String providerId;

    @Column(nullable = false)
    private String role;

    @Column(name = "create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

}
