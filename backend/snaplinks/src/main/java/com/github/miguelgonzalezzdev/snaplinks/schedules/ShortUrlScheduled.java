package com.github.miguelgonzalezzdev.snaplinks.schedules;

import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShortUrlScheduled {

    private final ShortUrlRepository shortUrlRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Todos los dias a las 03:00
    @Transactional
    public void expiredUrlsCleaner() {
        int deleted = shortUrlRepository.deleteExpiredUrls();
    }
}
