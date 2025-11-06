package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.GeoIpInfo;
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
    private final GeoIpService geoIpService;

    public void registerUrlAccessLog(ShortUrl shortUrl, HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String clientIp = getClientIp(request);
        GeoIpInfo geoInfo = geoIpService.getGeoIpInfo(clientIp);

        UrlAccessLog log = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now())
                .ipAddress(hashIp(clientIp))
                .userAgent(userAgent)
                .countryIso(geoInfo.countryIso())
                .countryName(geoInfo.countryName())
                .city(geoInfo.city())
                .browser(detectBrowser(userAgent))
                .deviceType(detectDeviceType(userAgent))
                .build();

        urlAccessLogRepository.save(log);
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

    private String detectBrowser(String ua) {
        if (ua == null) return "Unknown";
        ua = ua.toLowerCase();
        if (ua.contains("chrome") && !ua.contains("edg")) return "Chrome";
        if (ua.contains("firefox")) return "Firefox";
        if (ua.contains("safari") && !ua.contains("chrome")) return "Safari";
        if (ua.contains("edg")) return "Edge";
        if (ua.contains("opera") || ua.contains("opr")) return "Opera";
        return "Unknown";
    }

    private String detectDeviceType(String ua) {
        if (ua == null) return "Unknown";
        ua = ua.toLowerCase();
        if (ua.contains("mobile") || ua.contains("iphone") || ua.contains("android")) return "Mobile";
        if (ua.contains("ipad") || ua.contains("tablet")) return "Tablet";
        return "Desktop";
    }
}
