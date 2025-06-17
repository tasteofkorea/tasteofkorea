package com.example.tasteofkorea.entity;


public enum UserRoleEnum {
    OWNER,     // 식당 등록자
    ADMIN;     // 관리자

    public String getAuthority() {
        return "ROLE_" + this.name(); // 예: ROLE_USER, ROLE_ADMIN
    }
}
