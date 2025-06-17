package com.example.tasteofkorea.dto;

import com.example.tasteofkorea.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String introduce;
    private UserRoleEnum role;
}
