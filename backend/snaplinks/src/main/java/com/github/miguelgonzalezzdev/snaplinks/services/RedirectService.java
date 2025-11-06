package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.RedirectResponse;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.UrlAccessLog;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UrlAccessLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlAccessLogService urlAccessLogService;

    @Transactional()
    public RedirectResponse getOriginalUrlByShortCode(String shortCode, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Optional<ShortUrl> urlOpt = shortUrlRepository.findByShortCodeAndExpiresAtIsNullOrShortCodeAndExpiresAtAfter(shortCode, shortCode, now);

        ShortUrl url = urlOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "URL no encontrada")
        );

        urlAccessLogService.registerUrlAccessLog(url, request);

        return new RedirectResponse(url.getOriginalUrl());
    }
}
