package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.dtos.UrlStatsResponse;
import com.github.miguelgonzalezzdev.snaplinks.mappers.ShortUrlMapper;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.repositories.ShortUrlRepository;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UrlAccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlStatsService {

    private final ShortUrlService shortUrlService;
    private final UrlAccessLogRepository urlAccessLogRepository;

    public UrlStatsResponse getStats(Long id) {

        ShortUrl url = shortUrlService.findUrlEntityById(id);

        Long urlId = url.getId();
        String shortCode = url.getShortCode();
        LocalDateTime createdAt = url.getCreatedAt();

        long total = urlAccessLogRepository.countByShortUrlId(urlId);
        var firstAccess = urlAccessLogRepository.findFirstAccess(urlId);
        var lastAccess = urlAccessLogRepository.findLastAccess(urlId);

        Map<String, Long> byCountry = urlAccessLogRepository.countByCountry(urlId)
                .stream().collect(Collectors.toMap(
                        row -> (String) row[0] == null ? "Desconocido" : (String) row[0],
                        row -> (Long) row[1]
                ));

        Map<String, Long> byBrowser = urlAccessLogRepository.countByBrowser(urlId)
                .stream().collect(Collectors.toMap(
                        row -> (String) row[0] == null ? "Desconocido" : (String) row[0],
                        row -> (Long) row[1]
                ));

        Map<String, Long> byDevice = urlAccessLogRepository.countByDeviceType(urlId)
                .stream().collect(Collectors.toMap(
                        row -> (String) row[0] == null ? "Desconocido" : (String) row[0],
                        row -> (Long) row[1]
                ));

        return new UrlStatsResponse(
                urlId,
                shortCode,
                createdAt,
                total,
                lastAccess,
                byCountry,
                byBrowser,
                byDevice
        );
    }
}
