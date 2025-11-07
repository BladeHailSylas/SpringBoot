package com.example.bbs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column
    private String email;

    @Column(nullable = false, length = 20)
    private String role = "USER"; // 기본 USER 역할

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 생성자
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.email = null;
        this.role = role;
    }
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
