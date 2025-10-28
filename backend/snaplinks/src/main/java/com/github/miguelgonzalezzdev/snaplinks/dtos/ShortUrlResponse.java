package com.github.miguelgonzalezzdev.snaplinks.dtos;

import java.time.LocalDateTime;

public record ShortUrlResponse(Long id, String name, String shortCode, String originalUrl, LocalDateTime expiresAt, String qrCode) {
}
