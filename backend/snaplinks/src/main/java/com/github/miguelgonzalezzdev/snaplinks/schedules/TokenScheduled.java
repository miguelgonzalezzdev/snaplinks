package com.github.miguelgonzalezzdev.snaplinks.schedules;

import com.github.miguelgonzalezzdev.snaplinks.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TokenScheduled {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Europe/Madrid") // Todos los dias a las 02:00
    @Transactional
    public void revokedOrExpiredTokensCleaner() {
        int deleted = tokenRepository.deleteExpiredOrRevokedTokens();
    }
}
