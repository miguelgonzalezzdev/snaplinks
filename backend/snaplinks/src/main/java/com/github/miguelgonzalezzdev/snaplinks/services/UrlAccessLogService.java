package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import com.github.miguelgonzalezzdev.snaplinks.models.UrlAccessLog;
import com.github.miguelgonzalezzdev.snaplinks.repositories.UrlAccessLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UrlAccessLogService {

    private final UrlAccessLogRepository urlAccessLogRepository;

    public void registerUrlAccessLog(ShortUrl shortUrl, HttpServletRequest request) {

        UrlAccessLog accessLog = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now())
                .ipAddress(hashIp(getClientIp(request)))
                .userAgent(request.getHeader("User-Agent"))
                .build();

        urlAccessLogRepository.save(accessLog);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String hashIp(String ip) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ip.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing IP", e);
        }
    }
}
