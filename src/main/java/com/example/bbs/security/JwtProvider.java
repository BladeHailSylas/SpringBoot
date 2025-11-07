package com.example.bbs.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "ThisIsASecretKeyForJwtGeneration1234567890";
    private static final long EXPIRATION_MS = 1000L * 60 * 60; // 1시간

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 토큰 생성
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username) // setSubject → subject() 로 변경
                .claim("role", role)
                .issuedAt(new Date()) // setIssuedAt → issuedAt() 로 변경
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // setExpiration → expiration() 로 변경
                .signWith(key, Jwts.SIG.HS256) // SignatureAlgorithm.HS256 → Jwts.SIG.HS256 로 변경
                .compact();
    }

    /**
     * 토큰에서 사용자명 추출
     */
    public String getUsername(String token) {
        return parseClaims(token).getPayload().getSubject();
    }

    /**
     * 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 내부 파싱 메서드
     */
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key) // verifyWith로 키 등록
                .build()
                .parseSignedClaims(token);
    }
}
