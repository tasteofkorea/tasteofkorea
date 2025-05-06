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
    private UserRoleEnum role;  // 문자열이 아닌 Enum으로 직접 사용

    // 필요시 validation을 추가할 수 있습니다
}
