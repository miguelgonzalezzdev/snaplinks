package com.github.miguelgonzalezzdev.snaplinks.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record UrlStatsResponse(
        Long id,
        String shortCode,
        LocalDateTime createdAt,
        long totalAccesses,
        LocalDateTime lastAccess,
        Map<String, Long> accessesByCountry,
        Map<String, Long> accessesByBrowser,
        Map<String, Long> accessesByDeviceType
) {

}
