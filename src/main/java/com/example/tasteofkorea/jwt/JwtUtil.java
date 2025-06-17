package com.example.tasteofkorea.jwt;

import com.example.tasteofkorea.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        this.secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public String getUsernameFromBearer(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = bearerToken.substring(7);

        if (!validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
        }

        return getUsername(token);
    }
    public String createJwt(String nickname, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("nickname", nickname)
            .claim("role", role)
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("nickname", String.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }

    public boolean isExpired(String token) {
        Date expiration = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("JWT 서명 오류");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료됨");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 JWT");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims 문자열이 비어 있음");
        }
        return false;
    }
}
