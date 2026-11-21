package com.aistudy.backend.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 签发与校验。
 *
 * 密钥来自环境变量 AI_STUDY_JWT_SECRET_BASE64，不提供默认密钥。
 * Base64 解码后必须至少 32 字节（HS256）。
 */
@Component
public class JwtUtil {

    private static final long TOKEN_TTL_MS = 24L * 60 * 60 * 1000;
    private final Key key;

    public JwtUtil(@Value("${app.jwt.secret-base64}") String secretBase64) {
        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalArgumentException("请配置 AI_STUDY_JWT_SECRET_BASE64");
        }

        byte[] secretBytes;
        try {
            secretBytes = Decoders.BASE64.decode(secretBase64.trim());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("JWT 密钥必须是合法的 Base64 字符串", e);
        }

        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT 密钥解码后必须至少为 32 字节");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + TOKEN_TTL_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Integer getUserId(String token) {
        String id = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Integer.valueOf(id);
    }
}
