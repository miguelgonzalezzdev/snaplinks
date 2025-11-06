package com.github.miguelgonzalezzdev.snaplinks.services;

import com.github.miguelgonzalezzdev.snaplinks.dtos.GeoIpInfo;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoIpService {

    private final DatabaseReader databaseReader;

    // Obtiene los datos geograficos basicos de una IP (pais, ciudad, etc.)
    public GeoIpInfo getGeoIpInfo(String ip) {
        if (ip == null || ip.isBlank()) {
            return GeoIpInfo.empty();
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);

            // Ignorar IPs privadas o locales
            if (isPrivateIp(inetAddress)) {
                return GeoIpInfo.empty();
            }

            CityResponse response = databaseReader.city(inetAddress);

            String countryIso = Optional.ofNullable(response.getCountry())
                    .map(c -> c.getIsoCode())
                    .orElse(null);

            String countryName = Optional.ofNullable(response.getCountry())
                    .map(c -> c.getName())
                    .orElse(null);

            String region = Optional.ofNullable(response.getMostSpecificSubdivision())
                    .map(s -> s.getName())
                    .orElse(null);

            String city = Optional.ofNullable(response.getCity())
                    .map(c -> c.getName())
                    .orElse(null);

            return new GeoIpInfo(countryIso, countryName, region, city);

        } catch (IOException | GeoIp2Exception e) {
            log.warn("No se pudo obtener geolocalización para IP: {}", ip, e);
            return GeoIpInfo.empty();
        }
    }

    private boolean isPrivateIp(InetAddress addr) {
        return addr.isAnyLocalAddress()
                || addr.isLoopbackAddress()
                || addr.isSiteLocalAddress() // 10.x.x.x, 172.16.x.x, 192.168.x.x
                || addr.isLinkLocalAddress()
                || isIPv6UniqueLocal(addr);
    }

    private boolean isIPv6UniqueLocal(InetAddress addr) {
        String s = addr.getHostAddress();
        return s != null && (s.startsWith("fc") || s.startsWith("fd"));
    }
}
