package com.example.tasteofkorea.dto;

import com.example.tasteofkorea.entity.User;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String email;
    private String introduce;

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }
}
