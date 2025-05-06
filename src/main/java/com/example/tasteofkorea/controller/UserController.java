package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.dto.SignupRequestDto;
import com.example.tasteofkorea.dto.UpdateInfoRequestDto;
import com.example.tasteofkorea.dto.UserInfoResponseDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.example.tasteofkorea.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public Optional<User> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserInfo(userDetails);
    }

    //회원 소개 수정
    @PutMapping("/user-info")
    @ResponseBody
    public UserInfoResponseDto updateUserInfo(@RequestBody UpdateInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserInfo(requestDto, userDetails);
    }

    //회원탈퇴
    @PutMapping("/withdrawal")
    @ResponseBody
    public void withdrawal(@RequestBody JoinRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawal(requestDto, userDetails);
    }

    //로그아웃
    @DeleteMapping("/logout")
    @ResponseBody
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails);
    }


}
