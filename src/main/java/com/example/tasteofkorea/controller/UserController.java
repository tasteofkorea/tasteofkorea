package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.annotation.LoginUser;
import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.dto.SignupRequestDto;
import com.example.tasteofkorea.dto.UpdateInfoRequestDto;
import com.example.tasteofkorea.dto.UserInfoResponseDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.example.tasteofkorea.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;  // RedisTemplate 주입

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@LoginUser User user) {
        if (user == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("로그인된 사용자만 로그아웃 가능합니다.");
        }

        log.info("로그아웃 요청 사용자: {}", user.getUsername());

        // Redis에서 해당 사용자의 토큰 삭제
        String tokenKey = "JWT_TOKEN:" + user.getUsername();
        String token = redisTemplate.opsForValue().get(tokenKey);

        if (token != null) {
            // Redis에서 토큰을 블랙리스트에 등록 (로그아웃 처리)
            redisTemplate.opsForValue().set("BLACKLIST:" + token, "logout", 3600, TimeUnit.SECONDS);  // 1시간 동안 유효
            redisTemplate.delete(tokenKey);  // Redis에서 토큰 삭제
            log.info("로그아웃 처리 완료: Redis에서 토큰 삭제 및 블랙리스트 추가됨.");
        }

        return ResponseEntity.ok("로그아웃 완료. 클라이언트에서 토큰을 삭제해주세요.");
    }







    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDto> getMyInfo(@LoginUser User user) {
        return ResponseEntity.ok(new UserInfoResponseDto(
                user.getId(), user.getUsername(), user.getEmail(), user.getIntroduce()
        ));
    }




}
