package com.github.miguelgonzalezzdev.snaplinks.schedules;

import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ShortUrlScheduledTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlScheduled shortUrlScheduled;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------
    // ExpiredUrlsCleaner
    // ------------------------------------------------------------
    @Test
    void limpiarUrlsExpiradas_shouldCallRepository() {

        // Given
        when(shortUrlRepository.deleteExpiredUrls()).thenReturn(5);

        // When
        shortUrlScheduled.expiredUrlsCleaner();

        // Then
        verify(shortUrlRepository, times(1)).deleteExpiredUrls();
    }
}
