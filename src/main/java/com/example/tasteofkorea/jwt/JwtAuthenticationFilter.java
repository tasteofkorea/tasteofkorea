package com.example.tasteofkorea.jwt;

import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.entity.UserRoleEnum;
import com.example.tasteofkorea.jwt.JwtUtil;
import com.example.tasteofkorea.repository.UserRepository;
import com.example.tasteofkorea.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;  // RedisTemplate 주입

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        setFilterProcessesUrl("/api/user/login");
    }



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
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // JWT 토큰 생성
        String token = jwtUtil.createToken(username, role);

        // 로그아웃된 토큰을 체크
        if (redisTemplate.hasKey("BLACKLIST:" + token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"로그아웃된 토큰입니다.\"}");
            response.getWriter().flush();
            return;
        }

        // 응답 헤더에 토큰 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // JSON 응답 형식으로 토큰 추가
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\":\"" + token + "\"}");
        response.getWriter().flush();

        // 사용자 정보 업데이트
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다")));

        user.get().setToken(token);
        userRepository.save(user.get());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
