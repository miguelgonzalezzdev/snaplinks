package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.RedirectResponse;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class RedirectServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlAccessLogService urlAccessLogService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RedirectService redirectService;

    private ShortUrl url;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        url = ShortUrl.builder()
                .id(10L)
                .name("GitHub")
                .shortCode("abc123")
                .originalUrl("https://github.com/")
                .createdAt(LocalDateTime.now().minusDays(1))
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();
    }

    @Test
    void getOriginalUrlByShortCode_whenUrlExists_returnsRedirectResponse() {

        // Simular datos
        when(shortUrlRepository.findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(
                eq("abc123"), eq("abc123"), any(LocalDateTime.class))
        ).thenReturn(Optional.of(url));

        // Ejecutar el metodo a testear
        RedirectResponse response = redirectService.getOriginalUrlByShortCode("abc123", request);

        // Comprobar resultados
        assertThat(response).isNotNull();
        assertThat(response.originalUrl()).isEqualTo("https://github.com/");

        verify(shortUrlRepository).findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(
                eq("abc123"), eq("abc123"), any(LocalDateTime.class)
        );
        verify(urlAccessLogService).registerUrlAccessLog(eq(url), eq(request));
        verifyNoMoreInteractions(urlAccessLogService);
    }

    @Test
    void getOriginalUrlByShortCode_whenUrlNotFound_throwsResponseStatusException() {

        // Simular datos
        when(shortUrlRepository.findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(
                eq("notfound"), eq("notfound"), any(LocalDateTime.class))
        ).thenReturn(Optional.empty());

        // Ejecutar el metodo a testear y comprobar resultados
        assertThatThrownBy(() ->
                redirectService.getOriginalUrlByShortCode("notfound", request)
        )
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("URL no encontrada");

        verify(shortUrlRepository).findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(
                eq("notfound"), eq("notfound"), any(LocalDateTime.class)
        );
        verifyNoInteractions(urlAccessLogService);
    }
}
