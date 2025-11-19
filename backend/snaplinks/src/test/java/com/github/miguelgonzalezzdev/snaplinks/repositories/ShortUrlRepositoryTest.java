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
    private ShortUrl expired1;
    private ShortUrl expired2;

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
                .shortCode("url1")
                .originalUrl("https://example.com")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(null) // no expira
                .build();
        shortUrlRepository.save(url1);

        url2 = ShortUrl.builder()
                .shortCode("url2")
                .originalUrl("https://example.org")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1)) // expira en el futuro
                .build();
        shortUrlRepository.save(url2);

        expired1 = ShortUrl.builder()
                .shortCode("exp1")
                .originalUrl("https://expired1.com")
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(10))
                .expiresAt(LocalDateTime.now().minusDays(5)) // expiro hace 5 dias
                .build();
        shortUrlRepository.save(expired1);

        expired2 = ShortUrl.builder()
                .shortCode("exp2")
                .originalUrl("https://expired2.com")
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(20))
                .expiresAt(LocalDateTime.now().minusDays(1)) // expiro ayer
                .build();
        shortUrlRepository.save(expired2);

    }

    @Test
    void testFindByShortCode_exists() {
        Optional<ShortUrl> found = shortUrlRepository.findByShortCode("url1");
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
        assertTrue(urls.stream().anyMatch(u -> u.getShortCode().equals("url1")));
        assertTrue(urls.stream().anyMatch(u -> u.getShortCode().equals("url2")));
    }

    @Test
    void testFindByShortCodeAndExpiresAt() {
        LocalDateTime now = LocalDateTime.now();
        Optional<ShortUrl> found = shortUrlRepository.findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter("url2", "url2", now);
        assertTrue(found.isPresent());
        assertEquals("url2", found.get().getShortCode());
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

    @Test
    void testDeleteExpiredUrls() {

        int deleted = shortUrlRepository.deleteExpiredUrls();

        assertEquals(2, deleted, "Debe eliminar exactamente 2 URLs expiradas");

        List<ShortUrl> remaining = shortUrlRepository.findAll();
        assertEquals(2, remaining.size(), "Deben quedar exactamente 2 URLs no expiradas");

        assertFalse(remaining.stream().anyMatch(u -> u.getShortCode().equals("exp1")), "exp1 debe haber sido eliminada");
        assertFalse(remaining.stream().anyMatch(u -> u.getShortCode().equals("exp2")), "exp2 debe haber sido eliminada");

        assertTrue(remaining.stream().anyMatch(u -> u.getShortCode().equals("url1")), "url1 debe permanecer");
        assertTrue(remaining.stream().anyMatch(u -> u.getShortCode().equals("url2")), "url2 debe permanecer");
    }
}
