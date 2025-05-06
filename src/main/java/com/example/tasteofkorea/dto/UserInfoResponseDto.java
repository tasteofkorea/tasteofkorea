package com.example.tasteofkorea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String username;
    private String email;
    private String introduce;
}
