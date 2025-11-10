package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class ShortUrlRepositoryTest {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private ShortUrl url1;
    private ShortUrl url2;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        shortUrlRepository.deleteAll();
        userRepository.deleteAll();

        // Crear un usuario
        user = User.builder()
                .name("Test User")
                .email("user@example.com")
                .password("password123")
                .build();
        user = userRepository.save(user);

        // Crear URLs
        url1 = ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://example.com")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(null) // no expira
                .build();

        url2 = ShortUrl.builder()
                .shortCode("def456")
                .originalUrl("https://example.org")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1)) // expira en el futuro
                .build();

        shortUrlRepository.save(url1);
        shortUrlRepository.save(url2);
    }

    @Test
    void testFindByShortCode_exists() {
        Optional<ShortUrl> found = shortUrlRepository.findByShortCode("abc123");
        assertTrue(found.isPresent());
        assertEquals(url1.getShortCode(), found.get().getShortCode());
    }

    @Test
    void testFindByShortCode_notExists() {
        Optional<ShortUrl> found = shortUrlRepository.findByShortCode("nonexistent");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllByUserIdAndExpiresAt() {
        LocalDateTime now = LocalDateTime.now();
        List<ShortUrl> urls = shortUrlRepository.findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(user.getId(), user.getId(), now);
        assertNotNull(urls);
        assertEquals(2, urls.size());
        assertTrue(urls.stream().anyMatch(u -> u.getShortCode().equals("abc123")));
        assertTrue(urls.stream().anyMatch(u -> u.getShortCode().equals("def456")));
    }

    @Test
    void testFindByShortCodeAndExpiresAt() {
        LocalDateTime now = LocalDateTime.now();
        Optional<ShortUrl> found = shortUrlRepository.findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter("def456", "def456", now);
        assertTrue(found.isPresent());
        assertEquals("def456", found.get().getShortCode());
    }

    @Test
    void testFindByIdAndUserIdAndExpiresAt() {
        LocalDateTime now = LocalDateTime.now();
        Optional<ShortUrl> found = shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(
                url1.getId(), user.getId(), url1.getId(), user.getId(), now
        );
        assertTrue(found.isPresent());
        assertEquals(url1.getId(), found.get().getId());
    }
}
