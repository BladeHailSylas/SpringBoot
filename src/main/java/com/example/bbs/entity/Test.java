package com.example.bbs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_entity")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    // 생성자
    public Test() {}
    public Test(String message) {
        this.message = message;
    }

    // getter/setter
    public Long getId() { return id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}