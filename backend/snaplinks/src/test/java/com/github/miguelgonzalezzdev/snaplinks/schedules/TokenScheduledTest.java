package com.github.miguelgonzalezzdev.snaplinks.schedules;

import com.github.miguelgonzalezzdev.snaplinks.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest
class TokenScheduledTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenScheduled tokenScheduled;

    @Test
    void shouldDeleteExpiredOrRevokedTokens_WhenTaskRuns() {
        when(tokenRepository.deleteExpiredOrRevokedTokens()).thenReturn(5);

        tokenScheduled.revokedOrExpiredTokensCleaner();

        verify(tokenRepository, times(1)).deleteExpiredOrRevokedTokens();
    }
}
