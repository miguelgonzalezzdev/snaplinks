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

        String clientIp = getClientIp(request);
        GeoIpInfo geoInfo = geoIpService.getGeoIpInfo(clientIp);

        System.out.println(geoInfo);

        UrlAccessLog accessLog = UrlAccessLog.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now())
                .ipAddress(hashIp(clientIp))
                .userAgent(request.getHeader("User-Agent"))
                .countryIso(geoInfo.countryIso())
                .countryName(geoInfo.countryName())
                .city(geoInfo.city())
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
