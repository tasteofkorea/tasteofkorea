package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.annotation.LoginUser;
import com.example.tasteofkorea.dto.*;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.jwt.JwtUtil;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.example.tasteofkorea.service.CookieService;
import com.example.tasteofkorea.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원 API", description = "회원가입, 로그인, 정보조회, 탈퇴, 로그아웃 API")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieService cookieService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    // ✅ 2. JWT 기반 회원 정보 조회
    @Operation(summary = "회원 정보 조회 (JWT 기반)")
    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@RequestHeader("Authorization") String bearerToken) {
        String username = jwtUtil.getUsernameFromBearer(bearerToken); // nickname → username 으로 변경 가능

        User userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(new UserInfoResponseDto(userInfo));
    }


    // ✅ 3. 회원 소개 수정
    @Operation(summary = "회원 소개 수정")
    @PatchMapping("/user-info")
    public ResponseEntity<UserInfoResponseDto> updateUserInfo(
        @RequestBody UpdateInfoRequestDto requestDto,
        @RequestHeader("Authorization") String bearerToken
    ) {
        String username = jwtUtil.getUsernameFromBearer(bearerToken); // nickname → username 으로 변경 가능
        return ResponseEntity.ok().body(userService.updateUserInfo(requestDto, username));
    }

    // ✅ 4. 회원 탈퇴
    @Operation(summary = "회원 탈퇴")
    @PostMapping("/withdrawal")
    public ResponseEntity withdrawal(@RequestHeader("Authorization") String bearerToken,HttpServletResponse response) {
        String username = jwtUtil.getUsernameFromBearer(bearerToken); // nickname → username 으로 변경 가능
        userService.withdrawal(username);
        Cookie  expireCookie =cookieService.expireCookie("refresh");
        response.addCookie(expireCookie);
        return ResponseEntity.ok().build();
    }

    // ✅ 5. 로그아웃
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken, HttpServletResponse response) {
        String username = jwtUtil.getUsernameFromBearer(bearerToken);
        log.info("로그아웃 요청 사용자: {}", username);

        response.addCookie(cookieService.expireCookie("refresh"));
        return ResponseEntity.ok("로그아웃 완료. 클라이언트 쿠키도 삭제됨.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueAccessToken(@CookieValue("refresh") String refreshToken, HttpServletResponse response) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }
        String role = jwtUtil.getRole(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String newAccessToken = jwtUtil.createJwt(username,role ,10 * 60 * 1000L);
        String newrefreshToken = jwtUtil.createJwt(username, role,60 * 60 * 1000L);

        Cookie refreshCookie = cookieService.createCookie("refresh",newrefreshToken,60 * 60 * 1000);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
