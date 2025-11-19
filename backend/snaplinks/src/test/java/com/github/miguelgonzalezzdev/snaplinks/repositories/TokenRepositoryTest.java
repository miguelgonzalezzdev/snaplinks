package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.Token;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Token token1;
    private Token token2;
    private Token validToken1;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        // Crear un usuario de prueba
        user = User.builder()
                .name("Test User")
                .email("user@example.com")
                .password("password123")
                .build();
        user = userRepository.save(user);

        // Crear tokens de prueba
        token1 = Token.builder()
                .token("token-1")
                .user(user)
                .revoked(false)
                .expired(true)
                .build();

        token2 = Token.builder()
                .token("token-2")
                .user(user)
                .revoked(true)
                .expired(false)
                .build();

        validToken1 = Token.builder()
                .token("valid-token-1")
                .user(user)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token1);
        tokenRepository.save(token2);
        tokenRepository.save(validToken1);
    }

    @Test
    void testFindByToken_exists() {
        Optional<Token> found = tokenRepository.findByToken("token-1");
        assertTrue(found.isPresent());
        assertEquals(token1.getToken(), found.get().getToken());
    }

    @Test
    void testFindByToken_notExists() {
        Optional<Token> found = tokenRepository.findByToken("non-existent");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllValidIsFalseOrRevokedIsFalseByUserId() {
        List<Token> tokens = tokenRepository.findAllByExpiredIsFalseOrRevokedIsFalseAndUserId(user.getId());
        assertNotNull(tokens);
        assertEquals(3, tokens.size());

        boolean containsToken1 = tokens.stream().anyMatch(t -> t.getToken().equals("token-1"));
        boolean containsToken2 = tokens.stream().anyMatch(t -> t.getToken().equals("token-2"));
        boolean containsValidToken1 = tokens.stream().anyMatch(t -> t.getToken().equals("valid-token-1"));

        assertTrue(containsToken1);
        assertTrue(containsToken2);
        assertTrue(containsValidToken1);
    }

    @Test
    void testDeleteExpiredOrRevokedTokens() {
        int deletedCount = tokenRepository.deleteExpiredOrRevokedTokens();

        assertEquals(2, deletedCount, "Debe eliminar token1 (expirado) y token2 (revocado)");

        List<Token> remainingTokens = tokenRepository.findAll();

        assertEquals(1, remainingTokens.size(), "Solo debe quedar el token válido");
        assertEquals("valid-token-1", remainingTokens.get(0).getToken());
    }
}
