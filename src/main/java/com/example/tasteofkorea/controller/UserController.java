package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.dto.SignupRequestDto;
import com.example.tasteofkorea.dto.UpdateInfoRequestDto;
import com.example.tasteofkorea.dto.UserInfoResponseDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.example.tasteofkorea.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController  // Swagger를 위해 @Controller → @RestController 변경
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원 API", description = "회원가입, 로그인, 정보조회, 탈퇴, 로그아웃 API")
public class UserController {

    private final UserService userService;

    //회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    // 회원 관련 정보 받기
    @Operation(summary = "회원 정보 조회")
    @GetMapping("/user-info")
    @ResponseBody
    public Optional<User> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserInfo(userDetails);
    }

    //회원 소개 수정
    @Operation(summary = "회원 소개 수정")
    @PutMapping("/user-info")
    @ResponseBody
    public UserInfoResponseDto updateUserInfo(@RequestBody UpdateInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserInfo(requestDto, userDetails);
    }

    //회원탈퇴
    @Operation(summary = "회원 탈퇴")
    @PutMapping("/withdrawal")
    @ResponseBody
    public void withdrawal(@RequestBody JoinRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawal(requestDto, userDetails);
    }

    //로그아웃
    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    @ResponseBody
    public void logout(@RequestBody JoinRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(requestDto, userDetails);
    }



}
