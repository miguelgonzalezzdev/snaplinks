package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.DemoShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlRequest;
import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.mappers.ShortUrlMapper;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.User;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private ShortUrlMapper shortUrlMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ShortUrlService shortUrlService;

    private User user;
    private ShortUrl url;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("user@test.com")
                .build();

        url = ShortUrl.builder()
                .id(1L)
                .name("Mi URL")
                .shortCode("abc123xyz")
                .originalUrl("https://github.com")
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMonths(1).with(LocalTime.MAX))
                .qrCode("CODIGO QR")
                .build();
    }

    @Test
    void getAllUrlsByUser_shouldReturnMappedList() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(any(), any(), any()))
                .thenReturn(List.of(url));
        when(shortUrlMapper.toDtoList(List.of(url)))
                .thenReturn(List.of(new ShortUrlResponse(1L, "Mi URL", "abc123xyz", "https://github.com", url.getExpiresAt(), "qrCodeBase64")));

        List<ShortUrlResponse> result = shortUrlService.getAllUrlsByUser();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).shortCode()).isEqualTo("abc123xyz");
        verify(shortUrlRepository).findAllByUserIdAndExpiresAtIsNullOrUserIdAndExpiresAtAfter(
                eq(user.getId()),
                eq(user.getId()),
                any()
        );
    }

    @Test
    void getUrlById_shouldReturnMappedUrl() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(anyLong(), any(), anyLong(), any(), any()))
                .thenReturn(Optional.of(url));
        when(shortUrlMapper.toDto(url)).thenReturn(new ShortUrlResponse(1L, "Mi URL", "abc123xyz", "https://github.com", url.getExpiresAt(), "qrCodeBase64"));

        ShortUrlResponse response = shortUrlService.getUrlById(1L);

        assertThat(response.shortCode()).isEqualTo("abc123xyz");
    }

    @Test
    void getUrlById_shouldThrowIfNotFound() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.findByIdAndUserIdAndExpiresAtIsNullOrIdAndUserIdAndExpiresAtAfter(anyLong(), any(), anyLong(), any(), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> shortUrlService.getUrlById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("URL no encontrada");
    }

    @Test
    void createShortUrl_shouldGenerateAndSaveUrl() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(i -> i.getArgument(0));
        when(shortUrlMapper.toDto(any(ShortUrl.class))).thenAnswer(i -> new ShortUrlResponse(
                1L,
                ((ShortUrl) i.getArgument(0)).getName(),
                ((ShortUrl) i.getArgument(0)).getShortCode(),
                ((ShortUrl) i.getArgument(0)).getOriginalUrl(),
                ((ShortUrl) i.getArgument(0)).getExpiresAt(),
                ((ShortUrl) i.getArgument(0)).getQrCode()
        ));

        ShortUrlRequest request = new ShortUrlRequest("Mi URL", "https://github.com");
        ShortUrlResponse response = shortUrlService.createShortUrl(request);

        assertThat(response.originalUrl()).isEqualTo("https://github.com");
        assertThat(response.shortCode()).isNotNull();
        assertThat(response.qrCode()).isNotNull();
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void deleteShortUrl_shouldReturnTrueIfDeleted() {
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.findById(1L)).thenReturn(Optional.of(url));

        boolean deleted = shortUrlService.deleteShortUrl(1L);

        assertThat(deleted).isTrue();
        verify(shortUrlRepository).delete(url);
    }

    @Test
    void deleteShortUrl_shouldReturnFalseIfNotOwner() {
        User anotherUser = User.builder().id(UUID.randomUUID()).build();
        url.setUser(anotherUser);

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(shortUrlRepository.findById(1L)).thenReturn(Optional.of(url));

        boolean deleted = shortUrlService.deleteShortUrl(1L);

        assertThat(deleted).isFalse();
        verify(shortUrlRepository, never()).delete(any());
    }

    @Test
    void createDemoShortUrl_shouldSaveDemoUrl() {
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(i -> i.getArgument(0));
        when(shortUrlMapper.toDto(any(ShortUrl.class))).thenAnswer(i -> {
            ShortUrl u = i.getArgument(0);
            return new ShortUrlResponse(u.getId(), u.getName(), u.getShortCode(), u.getOriginalUrl(), u.getExpiresAt(), u.getQrCode());
        });

        DemoShortUrlRequest request = new DemoShortUrlRequest("https://github.com");
        ShortUrlResponse response = shortUrlService.createDemoShortUrl(request);

        assertThat(response.originalUrl()).isEqualTo("https://github.com");
        assertThat(response.shortCode()).isNotNull();
        assertThat(response.qrCode()).isNotNull();
    }
}
