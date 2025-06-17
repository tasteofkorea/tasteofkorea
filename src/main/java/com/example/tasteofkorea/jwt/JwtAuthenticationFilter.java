package com.example.tasteofkorea.jwt;

import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.entity.UserRoleEnum;
import com.example.tasteofkorea.repository.UserRepository;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.example.tasteofkorea.service.CookieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CookieService cookieService;



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JoinRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), JoinRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole().toString();

        // JWT 토큰 생성
        String accessToken = jwtUtil.createJwt(username, role,10 * 60 * 1000L);
        String refreshToken = jwtUtil.createJwt(username, role,60 * 60 * 1000L);

        // 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        Cookie refreshCookie = cookieService.createCookie("refresh",refreshToken,60 * 60 * 1000);
        response.addCookie(refreshCookie);

        // 사용자 정보 업데이트
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다")));


        userRepository.save(user.get());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
