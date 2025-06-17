package com.example.tasteofkorea.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    private String username;
    private String password;

    // 필요시 추가 필드나 validation을 할 수 있습니다
}
