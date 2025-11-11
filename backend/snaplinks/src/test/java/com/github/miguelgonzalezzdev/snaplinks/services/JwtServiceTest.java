package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Crear una clave secreta válida (Base64 encoded)
        SecretKey key = io.jsonwebtoken.security.Keys.hmacShaKeyFor("supersecretkeysupersecretkey123456".getBytes());
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        // Inyectar propiedades @Value manualmente
        ReflectionTestUtils.setField(jwtService, "secretKey", base64Key);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000L * 60 * 10); // 10 min
        ReflectionTestUtils.setField(jwtService, "refreshToken", 1000L * 60 * 60 * 24); // 1 día

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("user@test.com")
                .password("encodedPass")
                .build();
    }

    @Test
    void generateToken_shouldContainExpectedClaims() {
        String token = jwtService.generateToken(user);

        Claims claims = Jwts.parser()
                .verifyWith(getKeyFromService())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("user@test.com");
        assertThat(claims.get("name")).isEqualTo("Test User");
        assertThat(claims.getId()).isEqualTo(user.getId().toString());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    void generateRefreshToken_shouldHaveLongerExpiration() {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Date expAccess = jwtService.extractExpiration(accessToken);
        Date expRefresh = jwtService.extractExpiration(refreshToken);

        assertThat(expRefresh).isAfter(expAccess);
    }

    @Test
    void extractUsername_shouldReturnEmailFromToken() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("user@test.com");
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(user);
        boolean valid = jwtService.isTokenValid(token, user);

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(user);

        User anotherUser = User.builder()
                .id(UUID.randomUUID())
                .name("Other User")
                .email("other@test.com")
                .password("encodedPass")
                .build();

        boolean valid = jwtService.isTokenValid(token, anotherUser);

        assertThat(valid).isFalse();
    }

    @Test
    void extractExpiration_shouldReturnFutureDate() {
        String token = jwtService.generateToken(user);
        Date expiration = jwtService.extractExpiration(token);

        assertThat(expiration).isAfter(new Date());
    }

    // Helper para obtener la clave privada inyectada
    private SecretKey getKeyFromService() {
        String base64Key = (String) ReflectionTestUtils.getField(jwtService, "secretKey");
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(decoded);
    }
}
