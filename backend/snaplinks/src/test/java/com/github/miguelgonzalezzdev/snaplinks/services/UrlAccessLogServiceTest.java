package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.GeoIpInfo;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.UrlAccessLog;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UrlAccessLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UrlAccessLogServiceTest {

    @Mock
    private UrlAccessLogRepository urlAccessLogRepository;

    @Mock
    private GeoIpService geoIpService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UrlAccessLogService urlAccessLogService;

    private ShortUrl shortUrl;

    @Captor
    private ArgumentCaptor<UrlAccessLog> logCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        shortUrl = ShortUrl.builder()
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
    void testRegisterUrlAccessLog_savesLogWithExpectedFields() throws NoSuchAlgorithmException {

        // Simular datos
        String ip = "192.168.0.1";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/123.0.0.0 Safari/537.36";

        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(ip);
        when(request.getHeader("User-Agent")).thenReturn(userAgent);
        when(geoIpService.getGeoIpInfo(ip)).thenReturn(
                new GeoIpInfo("ES", "Spain", "Madrid", "Madrid")
        );

        // Ejecutar el metodo a testear
        urlAccessLogService.registerUrlAccessLog(shortUrl, request);

        // Verificar resultados
        verify(urlAccessLogRepository).save(logCaptor.capture());
        UrlAccessLog saved = logCaptor.getValue();

        assertThat(saved.getShortUrl()).isEqualTo(shortUrl);
        assertThat(saved.getUserAgent()).isEqualTo(userAgent);
        assertThat(saved.getCountryIso()).isEqualTo("ES");
        assertThat(saved.getCountryName()).isEqualTo("Spain");
        assertThat(saved.getCity()).isEqualTo("Madrid");
        assertThat(saved.getBrowser()).isEqualTo("Chrome");
        assertThat(saved.getDeviceType()).isEqualTo("Desktop");

        // Comprobamos que el hash IP sea el SHA-256 base64 del ip original
        String expectedHash = Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("SHA-256").digest(ip.getBytes())
        );
        assertThat(saved.getIpAddress()).isEqualTo(expectedHash);
        assertThat(saved.getAccessedAt()).isNotNull();
    }

    @Test
    void testRegisterUrlAccessLog_withMobileUserAgent_detectsMobile() {

        // Simular datos
        String ip = "10.0.0.5";
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X)";

        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(ip);
        when(request.getHeader("User-Agent")).thenReturn(userAgent);

        when(geoIpService.getGeoIpInfo(ip)).thenReturn(
                new GeoIpInfo(null, null, null, null)
        );

        // Ejecutar el metodo a testear
        urlAccessLogService.registerUrlAccessLog(shortUrl, request);

        // Verificar resultados
        verify(urlAccessLogRepository).save(logCaptor.capture());
        UrlAccessLog saved = logCaptor.getValue();
        assertThat(saved.getDeviceType()).isEqualTo("Mobile");
        assertThat(saved.getBrowser()).isEqualTo("Unknown"); // no Chrome, Firefox, etc.
        assertThat(saved.getCountryIso()).isNull();
    }

    @Test
    void testRegisterUrlAccessLog_usesXForwardedForIfPresent() {

        // Simular datos
        String ip = "8.8.8.8, 1.1.1.1";

        when(request.getHeader("X-Forwarded-For")).thenReturn(ip);
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/122.0");
        when(geoIpService.getGeoIpInfo("8.8.8.8")).thenReturn(
                new GeoIpInfo("US", "United States", "New York", "New York")
        );

        // Ejecutar el metodo a testear
        urlAccessLogService.registerUrlAccessLog(shortUrl, request);

        // Verificar resultados
        verify(urlAccessLogRepository).save(logCaptor.capture());
        UrlAccessLog saved = logCaptor.getValue();
        assertThat(saved.getIpAddress()).isNotNull();
        assertThat(saved.getCountryIso()).isEqualTo("US");
    }
}
