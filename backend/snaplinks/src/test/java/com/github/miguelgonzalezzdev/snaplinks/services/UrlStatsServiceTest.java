package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.UrlStatsResponse;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UrlAccessLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UrlStatsServiceTest {

    @Mock
    private ShortUrlService shortUrlService;

    @Mock
    private UrlAccessLogRepository urlAccessLogRepository;

    @InjectMocks
    private UrlStatsService urlStatsService;

    private ShortUrl url;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        url = ShortUrl.builder()
                .id(41L)
                .name("Mi URL")
                .shortCode("kE8Apjck0k")
                .originalUrl("https://github.com/")
                .qrCode("QR CODE")
                .createdAt(LocalDateTime.of(2025, 11, 5, 19, 13, 53))
                .expiresAt(LocalDateTime.of(2025, 12, 5, 23, 59, 59))
                .build();
    }

    @Test
    void testGetStats_returnsCorrectResponse() {

        // Simular datos
        when(shortUrlService.findUrlEntityById(41L)).thenReturn(url);
        when(urlAccessLogRepository.countByShortUrlId(41L)).thenReturn(6L);
        when(urlAccessLogRepository.findLastAccess(41L))
                .thenReturn(LocalDateTime.of(2025, 11, 10, 18, 54, 2));
        when(urlAccessLogRepository.countByCountry(41L))
                .thenReturn(List.of(
                        new Object[]{"Desconocido", 5L},
                        new Object[]{"Spain", 1L}
                ));
        when(urlAccessLogRepository.countByBrowser(41L))
                .thenReturn(List.of(
                        new Object[]{"Chrome", 3L},
                        new Object[]{"Desconocido", 3L}
                ));
        when(urlAccessLogRepository.countByDeviceType(41L))
                .thenReturn(List.of(
                        new Object[]{"Desktop", 3L},
                        new Object[]{"Desconocido", 3L}
                ));

        // Ejecutar el metodo a testear
        UrlStatsResponse result = urlStatsService.getStats(41L);

        // Verificar resultados
        assertThat(result.id()).isEqualTo(41L);
        assertThat(result.shortCode()).isEqualTo("kE8Apjck0k");
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2025, 11, 5, 19, 13, 53));
        assertThat(result.totalAccesses()).isEqualTo(6L);
        assertThat(result.lastAccess()).isEqualTo(LocalDateTime.of(2025, 11, 10, 18, 54, 2));
        assertThat(result.accessesByCountry()).isEqualTo(Map.of(
                "Desconocido", 5L,
                "Spain", 1L
        ));
        assertThat(result.accessesByBrowser()).isEqualTo(Map.of(
                "Chrome", 3L,
                "Desconocido", 3L
        ));
        assertThat(result.accessesByDeviceType()).isEqualTo(Map.of(
                "Desktop", 3L,
                "Desconocido", 3L
        ));

        verify(shortUrlService).findUrlEntityById(41L);
        verify(urlAccessLogRepository).countByShortUrlId(41L);
        verify(urlAccessLogRepository).findLastAccess(41L);
        verify(urlAccessLogRepository).countByCountry(41L);
        verify(urlAccessLogRepository).countByBrowser(41L);
        verify(urlAccessLogRepository).countByDeviceType(41L);
    }
}
