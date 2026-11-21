package com.aistudy.backend.common;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static JwtUtil createWithRandomKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return new JwtUtil(Base64.getEncoder().encodeToString(key));
    }

    @Test
    void shouldGenerateAndParseToken() {
        JwtUtil jwtUtil = createWithRandomKey();
        String token = jwtUtil.generateToken(123);
        assertEquals(123, jwtUtil.getUserId(token));
    }

    @Test
    void shouldRejectMissingOrShortSecrets() {
        assertThrows(IllegalArgumentException.class, () -> new JwtUtil(""));
        assertThrows(IllegalArgumentException.class, () -> new JwtUtil("not-base64!*"));
        assertThrows(IllegalArgumentException.class,
                () -> new JwtUtil(Base64.getEncoder().encodeToString(new byte[16])));
    }

    @Test
    void shouldRejectTokenSignedWithDifferentKey() {
        String token = createWithRandomKey().generateToken(123);
        assertThrows(JwtException.class, () -> createWithRandomKey().getUserId(token));
    }
}
