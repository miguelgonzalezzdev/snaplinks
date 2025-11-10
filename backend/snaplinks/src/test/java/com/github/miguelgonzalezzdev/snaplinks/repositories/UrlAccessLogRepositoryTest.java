package com.github.miguelgonzalezzdev.snaplinks.repositories;

import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.UrlAccessLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class UrlAccessLogRepositoryTest {

    @Autowired
    private UrlAccessLogRepository urlAccessLogRepository;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private ShortUrl shortUrl;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        urlAccessLogRepository.deleteAll();
        shortUrlRepository.deleteAll();

        // Crear ShortUrl de prueba
        shortUrl = ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://example.com")
                .createdAt(LocalDateTime.now())
                .build();
        shortUrl = shortUrlRepository.save(shortUrl);

        // Crear logs de acceso
        UrlAccessLog log1 = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now().minusDays(2))
                .countryName("Spain")
                .browser("Chrome")
                .deviceType("Desktop")
                .build();

        UrlAccessLog log2 = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now().minusDays(1))
                .countryName("Spain")
                .browser("Firefox")
                .deviceType("Mobile")
                .build();

        UrlAccessLog log3 = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now())
                .countryName("USA")
                .browser("Chrome")
                .deviceType("Desktop")
                .build();

        urlAccessLogRepository.saveAll(List.of(log1, log2, log3));
    }

    @Test
    void testCountByShortUrlId() {
        long count = urlAccessLogRepository.countByShortUrlId(shortUrl.getId());
        assertEquals(3, count);
    }

    @Test
    void testFindFirstAccess() {
        LocalDateTime first = urlAccessLogRepository.findFirstAccess(shortUrl.getId());
        assertNotNull(first);
        assertTrue(first.isBefore(LocalDateTime.now()));
    }

    @Test
    void testFindLastAccess() {
        LocalDateTime last = urlAccessLogRepository.findLastAccess(shortUrl.getId());
        assertNotNull(last);
        assertTrue(last.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testCountByCountry() {
        List<Object[]> results = urlAccessLogRepository.countByCountry(shortUrl.getId());
        assertEquals(2, results.size()); // Spain y USA

        boolean spainFound = results.stream()
                .anyMatch(r -> "Spain".equals(r[0]) && (Long) r[1] == 2L);
        boolean usaFound = results.stream()
                .anyMatch(r -> "USA".equals(r[0]) && (Long) r[1] == 1L);

        assertTrue(spainFound);
        assertTrue(usaFound);
    }

    @Test
    void testCountByBrowser() {
        List<Object[]> results = urlAccessLogRepository.countByBrowser(shortUrl.getId());
        assertEquals(2, results.size()); // Chrome y Firefox
    }

    @Test
    void testCountByDeviceType() {
        List<Object[]> results = urlAccessLogRepository.countByDeviceType(shortUrl.getId());
        assertEquals(2, results.size()); // Desktop y Mobile
    }
}
