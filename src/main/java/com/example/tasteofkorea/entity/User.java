package com.example.tasteofkorea.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "users")
public class User   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    @Email
    private String email;
    private String introduce;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
    private String token;
    private LocalDateTime lastLogin;



    public User(String username, String password, String name, String email, String introduce, UserRoleEnum role, String token) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.introduce = introduce;
        this.role = role;
        this.token = token;
        this.lastLogin = LocalDateTime.now();
    }

    public User changeIntroduce(String newIntroduce) {
        this.introduce = newIntroduce;
        return this;
    }
}
