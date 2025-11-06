package com.github.miguelgonzalezzdev.snaplinks.dtos;

public record GeoIpInfo(
        String countryIso,
        String countryName,
        String region,
        String city
) {
    public static GeoIpInfo empty() {
        return new GeoIpInfo(null, null, null, null);
    }
}
